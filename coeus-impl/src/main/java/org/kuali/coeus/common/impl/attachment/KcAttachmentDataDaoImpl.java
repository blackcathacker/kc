package org.kuali.coeus.common.impl.attachment;

import org.kuali.coeus.common.framework.attachment.KcAttachmentDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;

@Component("kcAttachmentDataDao")
public class KcAttachmentDataDaoImpl implements KcAttachmentDataDao{

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Override
    public byte[] getData(String tableName, String columnName, Map<String,String> parameters) {
        try(Connection connection = getDataSource().getConnection()) {
            String queryStart = "SELECT " + columnName + " FROM " + tableName;
            try (PreparedStatement stmt = connection.prepareStatement(prepareQuery(queryStart,parameters))){
                return getBytesFromBlob(addParameters(stmt,parameters,1).executeQuery());
            } catch (Exception e) {
                throw new RuntimeException("Error retrieving attachment data",e);
            }
        }catch (Exception e) {
            throw new RuntimeException("Unable to establish connection",e);
        }
    }

    @Override
    public void setData(String tableName, String columnName, Map<String,String> parameters, byte[] attachmentData) {
        try (Connection connection = getDataSource().getConnection()) {
            String queryStart = "UPDATE " + tableName + " SET " + columnName + " = ?";
            try (PreparedStatement stmt = addParameters(addBlob(connection.prepareStatement(prepareQuery(queryStart, parameters)), attachmentData), parameters, 2)) {
                stmt.executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException("Error updating attachment data",e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to establish connection",e);
        }

    }
    protected String prepareQuery(String queryStart, Map<String,String> parameters) {
        StringBuilder queryString = new StringBuilder(queryStart);
        if (!parameters.isEmpty()) {
            queryString.append(" WHERE ");
            for (Iterator iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<String,String> entry = (Map.Entry<String,String>) iterator.next();
                queryString.append(entry.getKey() + "=?");
                if (iterator.hasNext()) {
                    queryString.append( " AND ");
                }
            }
        }
        return queryString.toString();
    }

    protected PreparedStatement addParameters(PreparedStatement stmt, Map<String,String> parameters, int start) throws Exception {
        int parameterIndex = start ;
        for (Iterator iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String,String> entry = (Map.Entry<String,String>) iterator.next();
            stmt.setString(parameterIndex,entry.getValue());
            parameterIndex ++;
        }
        return  stmt;
    }

    protected PreparedStatement addBlob(PreparedStatement stmt,byte[] attachmentData) throws Exception {
        stmt.setBlob(1,new SerialBlob(attachmentData));
        return stmt;
    }

    protected byte[] getBytesFromBlob(ResultSet result) throws Exception {
        result.next();
        Blob blob = result.getBlob(1);
        int blobLength = (int) blob.length();
        return blob.getBytes(1,blobLength);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
