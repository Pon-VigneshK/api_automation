package org.op_ra.utils;

import org.op_ra.enums.DataBaseProperties;
import org.op_ra.exceptions.JsonExceptions;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class DatabaseQueryExecutor {
    private static Connection myConn;

    public static Connection connectToMySQL() throws SQLException, ClassNotFoundException, JsonExceptions {

        Class.forName("com.mysql.cj.jdbc.Driver");

        // Read DB config from JSON using JsonConfigUtils
        String hostname = JsonConfigUtils.get(DataBaseProperties.HOSTNAME);
        String port = JsonConfigUtils.get(DataBaseProperties.PORT);
        String schema = JsonConfigUtils.get(DataBaseProperties.SCHEMA);
        String username = StringDecoder.decodeBase64UTF8(JsonConfigUtils.get(DataBaseProperties.DBUSERNAME));
        String password = StringDecoder.decodeBase64UTF8(JsonConfigUtils.get(DataBaseProperties.DBPASSWORD));


        String url = String.format("jdbc:mysql://%s:%s/%s", hostname, port, schema);

        try {
            myConn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new SQLException("Error connecting to MySQL database.", e);
        }

        return myConn;
    }

    public static Map<String, String> retrieveRowData(String query) {
        Map<String, String> row = new HashMap<>();
        try (Connection connection = connectToMySQL();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            if (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getString(i));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return row;
    }
}
