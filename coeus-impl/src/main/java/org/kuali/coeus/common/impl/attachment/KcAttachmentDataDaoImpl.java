package org.kuali.coeus.common.impl.attachment;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.coeus.common.framework.attachment.KcAttachmentDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component("kcAttachmentDataDao")
public class KcAttachmentDataDaoImpl implements KcAttachmentDataDao {

    private static Log LOG = LogFactory.getLog(KcAttachmentDataDaoImpl.class);

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;
    
    private Set<TableReference> tableReferences;

    @Override
    public byte[] getData(String id) {
    	if (LOG.isDebugEnabled()) {
            LOG.debug("Fetching attachment data existing id: " + id);
        }

        if (StringUtils.isNotBlank(id)) {
	        try(Connection connection = getDataSource().getConnection();
	        		PreparedStatement stmt = connection.prepareStatement("select data from file_data where id = ?")) {
	        	stmt.setString(1, id);
	        	try (ResultSet rs = stmt.executeQuery()) {
	        		if (rs.next()) {
	        			return rs.getBytes(1);
	        		} else {
	        			return null;
	        		}
	        	}
	        } catch (SQLException e) {
	        	throw new RuntimeException(e);
			}
    	} else {
    		return null;
    	}
    }

    @Override
    public String saveData(byte[] attachmentData, String id) {
        if (ArrayUtils.isEmpty(attachmentData)) {
            throw new IllegalArgumentException("attachmentData is null");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Saving attachment data, existing id: " + id);
        }

        try (Connection connection = getDataSource().getConnection()) {
        	try (PreparedStatement stmt = connection.prepareStatement("insert into file_data (id, data) values (?, ?)")) {
	        	String newId = UUID.randomUUID().toString();
	        	stmt.setString(1, newId);
	        	stmt.setBlob(2, new SerialBlob(attachmentData));
	        	stmt.executeUpdate();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Created attachment data, new id: " + newId);
                }
                
            	if (StringUtils.isNotBlank(id)) {
            		deleteAttachment(connection, id);
            	}

                return newId;
        	}
        } catch (SQLException e) {
        	throw new RuntimeException(e);
        }
    }

    @Override
    public void removeData(String id) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Removing attachment data, existing id: " + id);
        }

        if (StringUtils.isNotBlank(id)) {
            try (Connection conn = dataSource.getConnection()) {
                deleteAttachment(conn, id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
        
    protected void deleteAttachment(Connection conn, String id) throws SQLException {
    	if (countReferences(conn, id) == 0) {
	    	try (PreparedStatement stmt = conn.prepareStatement("delete from file_data where id = ?")) {
	    		stmt.setString(1, id);
	    		stmt.executeUpdate();
	    	}
    	}
    }
    
    protected int countReferences(Connection conn, String id) throws SQLException {
    	if (tableReferences == null) {
    		populateReferences(conn);
    	}
    	int count = 0;
    	for (TableReference ref : tableReferences) {
        	try (PreparedStatement stmt = conn.prepareStatement("select count(*) from "
        			+ ref.tableName + " where " + ref.columnName + " = ?")) {
        		stmt.setString(1, id);
        		try (ResultSet rs = stmt.executeQuery()) {
        			if (rs.next()) {
        				count += rs.getInt(1);
        			}
        		}
        	}
    	}
    	return count;
    }
    
    protected void populateReferences(Connection conn) throws SQLException {
    	tableReferences = new HashSet<>();
    	try (PreparedStatement stmt = conn.prepareStatement(
    			"select table_name, column_name from information_schema.key_column_usage " 
    					+ " where lower(referenced_table_name) = 'file_data' and lower(referenced_column_name) = 'id' " 
    					+ " and table_schema = ?"
    			)) {
    		stmt.setString(1, "kcdev");
    		try (ResultSet rs = stmt.executeQuery()) {
    			while (rs.next()) {
    				tableReferences.add(new TableReference(rs.getString(1), rs.getString(2)));
    			}
    		}
    	} catch (Exception e) {
    		tableReferences = null;
    	}
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    class TableReference {
    	public String tableName;
		public String columnName;
    	public TableReference(String tableName, String columnName) {
			this.tableName = tableName;
			this.columnName = columnName;
		}
    	@Override
		public int hashCode() {
    		return new HashCodeBuilder(17, 37).append(tableName).append(columnName).toHashCode();
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final TableReference other = (TableReference) obj;
			return new EqualsBuilder().append(tableName, other.tableName).append(columnName, other.columnName).isEquals();
		}
    }

	public Set<TableReference> getTableReferences() {
		return tableReferences;
	}

	public void setTableReferences(Set<TableReference> tableReferences) {
		this.tableReferences = tableReferences;
	}
}
