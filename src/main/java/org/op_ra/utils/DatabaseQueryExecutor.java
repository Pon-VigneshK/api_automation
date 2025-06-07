package org.op_ra.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for executing SQL queries against a database.
 * This class provides methods to run SELECT, INSERT, UPDATE, and DELETE queries.
 * It uses a connection obtained from {@link DataBaseConnectionUtils}.
 * <p>
 * Methods are provided to fetch results from SELECT queries into a list of maps
 * (where each map represents a row) and to get the count of affected rows for
 * DML (Data Manipulation Language) queries.
 * </p>
 * Callers are responsible for ensuring the database connection is managed (opened/closed) appropriately,
 * though this class attempts to close Statements and ResultSets.
 */
public final class DatabaseQueryExecutor {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DatabaseQueryExecutor() {
        // Private constructor
    }

    /**
     * Executes a SELECT SQL query and returns the results as a list of maps.
     * Each map in the list represents a row, with column names as keys and row values as map values.
     *
     * @param query The SQL SELECT query string to execute.
     * @return A {@code List<Map<String, Object>>} containing the query results.
     *         Returns an empty list if the query yields no results or an error occurs.
     * @throws RuntimeException if a {@link SQLException} occurs during query execution.
     *                          The underlying SQLException is wrapped.
     */
    public static List<Map<String, Object>> executeSelectQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DataBaseConnectionUtils.getMyConn(); // Get connection
            if (conn == null || conn.isClosed()) {
                System.err.println("DatabaseQueryExecutor: Cannot execute query, database connection is null or closed.");
                return results; // Return empty list if no valid connection
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnLabel(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception during SELECT query execution: " + query + " - Error: " + e.getMessage());
            // Optionally log to FrameworkLogger or ExtentLogger
            // FrameworkLogger.log(LogType.ERROR, "SQL Exception on SELECT: " + e.getMessage());
            throw new RuntimeException("Failed to execute SELECT query: " + query, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                // Connection is typically managed by DataBaseConnectionUtils (e.g., closed at end of suite)
                // If not, it should be closed here or by the caller.
                // DataBaseConnectionUtils.closeConnection(); // If connection should be closed after each query
            } catch (SQLException e) {
                System.err.println("SQL Exception while closing resources: " + e.getMessage());
            }
        }
        return results;
    }

    /**
     * Executes a DML (Data Manipulation Language) SQL query such as INSERT, UPDATE, or DELETE.
     *
     * @param query The SQL DML query string to execute.
     * @return The number of rows affected by the query. Returns -1 if an error occurs and the
     *         operation might not have completed.
     * @throws RuntimeException if a {@link SQLException} occurs during query execution.
     */
    public static int executeUpdateQuery(String query) {
        Connection conn = null;
        Statement stmt = null;
        int affectedRows = -1; // Default to -1 to indicate potential failure or no rows affected
        try {
            conn = DataBaseConnectionUtils.getMyConn();
             if (conn == null || conn.isClosed()) {
                System.err.println("DatabaseQueryExecutor: Cannot execute update query, database connection is null or closed.");
                return affectedRows;
            }
            stmt = conn.createStatement();
            affectedRows = stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("SQL Exception during DML query execution: " + query + " - Error: " + e.getMessage());
            // FrameworkLogger.log(LogType.ERROR, "SQL Exception on DML: " + e.getMessage());
            throw new RuntimeException("Failed to execute DML query: " + query, e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                // DataBaseConnectionUtils.closeConnection(); // If connection should be closed after each query
            } catch (SQLException e) {
                 System.err.println("SQL Exception while closing statement: " + e.getMessage());
            }
        }
        return affectedRows;
    }

    // Consider adding methods for PreparedStatement for better security (prevent SQL injection)
    // and performance for repeated queries.
    // Example:
    // public static int executeUpdateWithPreparedStatement(String query, Object... params) { ... }
    // public static List<Map<String, Object>> executeSelectWithPreparedStatement(String query, Object... params) { ... }

}
