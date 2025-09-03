package org.op_ra.utils;

import org.op_ra.enums.DataBaseProperties;
import org.op_ra.exceptions.JsonExceptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code DatabaseQueryExecutor} is a utility class for executing SQL queries on a MySQL database.
 * This class provides methods for connecting to the database and retrieving data from it.
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class can be called to execute a query and retrieve the first row of the result set.</p>
 *
 * <pre>{@code
 * Map<String, String> data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM users WHERE id = 1");
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class DatabaseQueryExecutor {
    private static Connection myConn;

    /**
     * Establishes a connection to a MySQL database using the connection details from the configuration.
     *
     * @return A {@link Connection} object for the database.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the JDBC driver cannot be found.
     * @throws JsonExceptions if there is an error reading the database configuration.
     */
    public static Connection connectToMySQL() throws SQLException, ClassNotFoundException, JsonExceptions {

        Class.forName("com.mysql.cj.jdbc.Driver");

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

    /**
     * Executes a SQL query and returns the first row of the result set as a map.
     *
     * @param query The SQL query to execute.
     * @return A map representing the first row of the result set, where the keys are column names.
     */
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
        } catch (SQLException | ClassNotFoundException | JsonExceptions e) {
            e.printStackTrace();
        }
        return row;
    }
}
