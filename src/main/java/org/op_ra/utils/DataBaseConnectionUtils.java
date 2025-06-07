package org.op_ra.utils;

import org.op_ra.enums.ConfigProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Utility class for managing database connections.
 * It provides a method to establish a connection to a MySQL database
 * using connection details (URL, username, password) retrieved from configuration via {@link PropertyUtils}.
 * <p>
 * The connection is established on demand and should be closed by the caller after use.
 * </p>
 */
public final class DataBaseConnectionUtils {

    private static Connection myConn; // Static connection instance

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DataBaseConnectionUtils() {
        // Private constructor
    }

    /**
     * Establishes and returns a connection to the MySQL database.
     * Retrieves database URL, username, and password from configuration properties
     * ({@link ConfigProperties#DB_URL}, {@link ConfigProperties#DB_USERNAME}, {@link ConfigProperties#DB_PASSWORD}).
     * <p>
     * If a connection has already been established and is still valid, it returns the existing connection.
     * Otherwise, it attempts to create a new connection.
     * </p>
     *
     * @return A {@link java.sql.Connection} object representing the database connection.
     * @throws RuntimeException if a {@link SQLException} occurs while trying to connect to the database,
     *                          or if the database driver class is not found.
     */
    public static Connection getMyConn() {
        try {
            // Check if the connection is null or closed, then create a new one
            if (Objects.isNull(myConn) || myConn.isClosed()) {
                // Explicitly load the MySQL JDBC driver (optional for modern JDBC, but good practice for some environments)
                // Class.forName("com.mysql.cj.jdbc.Driver"); // Uncomment if facing driver not found issues

                myConn = DriverManager.getConnection(
                        PropertyUtils.getValue(ConfigProperties.DB_URL),
                        PropertyUtils.getValue(ConfigProperties.DB_USERNAME),
                        PropertyUtils.getValue(ConfigProperties.DB_PASSWORD)
                );
            }
        } catch (SQLException e) {
            // Log the error or wrap it in a custom unchecked exception
            System.err.println("Failed to connect to the database: " + e.getMessage());
            throw new RuntimeException("Failed to connect to the database.", e);
        }
        // catch (ClassNotFoundException e) {
        //     System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        //     throw new RuntimeException("MySQL JDBC Driver not found.", e);
        // }
        return myConn;
    }

    /**
     * Closes the current database connection if it is open.
     * It'''s important to call this method after database operations are complete
     * to release resources, especially if the connection is not managed by a connection pool.
     *
     * @throws RuntimeException if a {@link SQLException} occurs while closing the connection.
     */
    public static void closeConnection() {
        try {
            if (Objects.nonNull(myConn) && !myConn.isClosed()) {
                myConn.close();
                myConn = null; // Set to null after closing
                System.out.println("Database connection closed successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close the database connection: " + e.getMessage());
            throw new RuntimeException("Failed to close the database connection.", e);
        }
    }
}
