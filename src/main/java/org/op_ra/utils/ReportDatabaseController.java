package org.op_ra.utils;

import org.op_ra.enums.ConfigProperties;
import org.op_ra.enums.LogType;
import org.op_ra.reports.FrameworkLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

import static org.op_ra.constants.FrameworkConstants.getBranchName;

/**
 * {@code ReportDatabaseController} is a class that provides a method for logging test results
 * to a database. This class is responsible for inserting a new record into the execution log
 * table with details about the test run.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The {@code logResult} method can be called to log the result of a test to the database.</p>
 *
 * <pre>{@code
 * ReportDatabaseController.logResult(
 *     "run-id-123",
 *     1,
 *     "MyService",
 *     "myTestMethod",
 *     "Pass",
 *     "QA",
 *     1500L,
 *     200,
 *     "1.0.0",
 *     "manual-run",
 *     null,
 *     LocalDateTime.now()
 * );
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class ReportDatabaseController {

    private ReportDatabaseController() {
    }

    /**
     * Logs the result of a test execution to the database. The logging is only performed if the
     * "auditlogdb" property in the configuration is set to "true".
     *
     * @param runId The unique ID of the test run.
     * @param iterationId The iteration ID of the test.
     * @param serviceName The name of the service under test.
     * @param scriptName The name of the test script.
     * @param status The status of the test (e.g., "Pass", "Fail", "Skip").
     * @param environment The environment in which the test was run.
     * @param responseTimeMs The response time of the test in milliseconds.
     * @param httpStatusCode The HTTP status code of the response.
     * @param buildVersion The build version of the application.
     * @param triggeredBy Who triggered the test execution.
     * @param errorMessage The error message if the test failed.
     * @param executionTime The time at which the test was executed.
     */
    public static void logResult(String runId,
                                 int iterationId,
                                 String serviceName,
                                 String scriptName,
                                 String status,
                                 String environment,
                                 Long responseTimeMs,
                                 Integer httpStatusCode,
                                 String buildVersion,
                                 String triggeredBy,
                                 String errorMessage,
                                 LocalDateTime executionTime) {

        if (!"true".equalsIgnoreCase(PropertyUtils.getValue(ConfigProperties.AUDITLOGDB))) {
            return;
        }

        String insertQuery = "INSERT INTO `automation_execution_logs`.`qe_atlas_practice_execution_log` " +
                "(`runId`, `iterationId`, `serviceName`, `testCaseName`, `status`, `environment`, " +
                "`executionTime`, `responseTimeMs`, `httpStatusCode`, `buildVersion`, `triggeredBy`, `errorMessage`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataBaseConnectionUtils.getCloudConn();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            int idx = 1;
            statement.setString(idx++, runId);
            statement.setInt(idx++, iterationId);
            statement.setString(idx++, serviceName);
            statement.setString(idx++, scriptName);
            statement.setString(idx++, status);

            String envToLog = (environment == null || environment.isBlank()) ? getBranchName() : environment;
            statement.setString(idx++, envToLog);

            statement.setTimestamp(idx++, executionTime != null ? Timestamp.valueOf(executionTime) : Timestamp.valueOf(LocalDateTime.now()));

            if (responseTimeMs != null) statement.setLong(idx++, responseTimeMs);
            else statement.setNull(idx++, Types.BIGINT);

            if (httpStatusCode != null) statement.setInt(idx++, httpStatusCode);
            else statement.setNull(idx++, Types.INTEGER);

            statement.setString(idx++, buildVersion != null ? buildVersion : "");
            statement.setString(idx++, triggeredBy != null ? triggeredBy : "");
            statement.setString(idx++, errorMessage != null ? errorMessage : "");

            FrameworkLogger.log(LogType.INFO, "Executing DB insert: " + statement);

            statement.executeUpdate();

        } catch (SQLException e) {
            FrameworkLogger.log(LogType.WARN, "Could not store enriched test result in database: " + e.getMessage());
        }
    }
}
