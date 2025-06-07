package org.op_ra.utils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseQueryExecutor {
    public static Map<String, String> retrieveRowData(String query) {
        Map<String, String> row = new HashMap<>();
        try {
            Connection connection = DataBaseConnectionUtils.getMyConn();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getString(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

}
