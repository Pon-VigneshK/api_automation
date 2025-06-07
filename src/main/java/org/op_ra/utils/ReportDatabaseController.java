package org.op_ra.utils;

import org.op_ra.constants.FrameworkConstants;
import org.op_ra.enums.ConfigProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReportDatabaseController {
    /**
     * Private constructor to prevent instantiation of the ReportDatabaseController class.
     * <p>
     * This class is designed as a controller with static methods to handle database operations
     * related to storing test report entries.
     */
    private ReportDatabaseController() {
    }

    /**
     * Stores the test report entry into the database.
     * <p>
     * This method inserts a test case entry into the database only when the
     * application is running in local mode. It utilizes the configuration properties
     * to determine the runtime environment and prepares a SQL insert statement
     * to add the details of the test case to the database.
     *
     * @param testCaseName the name of the test case to be stored in the database
     * @param status       the status of the test case (e.g., passed, failed)
     */
    public static void storeReportInDatabase(String testCaseName, String status) {
        if (PropertyUtils.getValue(ConfigProperties.RUN_MODE).equalsIgnoreCase("local")) {
            String insertQuery = "INSERT INTO `service_result`.`fhir_service` (`environment`, `testCaseName`, `status`, `executionTime`) VALUES (?, ?, ?, ?)";
            try (Connection connection = DataBaseConnectionUtils.getMyConn();
                 PreparedStatement statement = ((Connection) connection).prepareStatement(insertQuery)) {
                if (connection == null) {
                    throw new SQLException("No database connection available.");
                }
                statement.setString(1, FrameworkConstants.getEnvironment().toUpperCase());
                statement.setString(2, testCaseName);
                statement.setString(3, status);
                statement.setString(4, LocalDateTime.now().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
