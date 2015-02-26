package org.kuali.coeus.common.framework.attachment;

import java.util.Map;

public interface KcAttachmentDataDao {
    public byte[] getData(String tableName, String columnName, Map<String, String> parameters);
    public void setData(String table, String columnName, Map<String,String> parameters, byte[] attachmentData);
}
