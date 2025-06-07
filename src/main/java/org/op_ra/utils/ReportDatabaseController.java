package org.op_ra.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Controls interactions with a database for storing test report summaries or results.
 * This class provides methods to insert test case outcomes (e.g., method name, status, execution time)
 * into a predefined database table.
 * <p>
 * It relies on {@link DataBaseConnectionUtils} to obtain a database connection.
 * </p>
 * The database table structure is assumed to include columns for:
 * - test_method_name (String)
 * - test_status (String - e.g., "PASS", "FAIL", "SKIP")
 * - execution_timestamp (Timestamp)
 * - (Optionally) execution_duration_ms (Long), suite_name (String), etc.
 */
public final class ReportDatabaseController {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ReportDatabaseController() {
        // Private constructor
    }

    /**
     * Stores the result of a test case into the reporting database.
     *
     * @param testMethodName The name of the test method (e.g., from {@code ITestResult.getMethod().getMethodName()}).
     * @param testStatus     The status of the test (e.g., "Pass", "Fail", "Skip").
     *                       Consider using an enum for status for consistency.
     */
    public static void storeReportInDatabase(String testMethodName, String testStatus) {
        // Example SQL INSERT statement. Adjust table and column names as per your database schema.
        String sql = "INSERT INTO test_execution_reports (test_method_name, test_status, execution_timestamp) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DataBaseConnectionUtils.getMyConn();
            if (conn == null || conn.isClosed()) {
                System.err.println("ReportDatabaseController: Cannot store report, database connection is null or closed.");
                return;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, testMethodName);
            pstmt.setString(2, testStatus);
            pstmt.setTimestamp(3, new Timestamp(new Date().getTime())); // Current timestamp

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("ReportDatabaseController: Test result for '" + testMethodName + "' stored successfully in database.");
                // FrameworkLogger.log(LogType.INFO, "Test result for '" + testMethodName + "' stored in DB.");
            } else {
                System.err.println("ReportDatabaseController: Failed to store test result for '" + testMethodName + "' in database (0 rows affected).");
                // FrameworkLogger.log(LogType.WARN, "Failed to store test result for '" + testMethodName + "' in DB.");
            }

        } catch (SQLException e) {
            System.err.println("ReportDatabaseController: SQL Exception while storing test report for '" + testMethodName + "': " + e.getMessage());
            // FrameworkLogger.log(LogType.ERROR, "SQL Exception storing report for '" + testMethodName + "': " + e.getMessage());
            // Consider whether to throw a runtime exception or just log the error.
            // throw new RuntimeException("Failed to store test report in database for " + testMethodName, e);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                // Connection is typically managed by DataBaseConnectionUtils.
                // If not, close it here: DataBaseConnectionUtils.closeConnection();
            } catch (SQLException e) {
                System.err.println("ReportDatabaseController: SQL Exception while closing PreparedStatement: " + e.getMessage());
            }
        }
    }

    // You might add other methods, for example:
    // - To create the reports table if it doesn'''t exist.
    // - To fetch aggregated report data from the database.
    // - To clean up old report data.
}
