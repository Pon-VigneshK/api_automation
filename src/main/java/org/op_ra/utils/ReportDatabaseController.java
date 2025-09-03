package org.op_ra.utils;

import org.op_ra.enums.ConfigProperties;
import org.op_ra.enums.LogType;
import org.op_ra.reports.FrameworkLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.sql.Timestamp;

import static org.op_ra.constants.FrameworkConstants.getBranchName;

public class ReportDatabaseController {

    /**
     * Private constructor to prevent instantiation of the ReportDatabaseController class.
     */
    private ReportDatabaseController() {
    }

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

            // environment from parameter, falling back to branchName if blank
            String envToLog = (environment == null || environment.isBlank()) ? getBranchName() : environment;
            statement.setString(idx++, envToLog);

            // execution time (proper timestamp)
            statement.setTimestamp(idx++, executionTime != null ? Timestamp.valueOf(executionTime) : Timestamp.valueOf(LocalDateTime.now()));

            // response time
            if (responseTimeMs != null) statement.setLong(idx++, responseTimeMs);
            else statement.setNull(idx++, Types.BIGINT);

            // http status
            if (httpStatusCode != null) statement.setInt(idx++, httpStatusCode);
            else statement.setNull(idx++, Types.INTEGER);

            // buildVersion, triggeredBy, errorMessage with null safety
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
