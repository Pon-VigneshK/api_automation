package org.op_ra.utils;

import org.op_ra.constants.FrameworkConstants;
import org.op_ra.enums.ConfigProperties;
import org.op_ra.enums.DataBaseProperties;
import org.op_ra.exceptions.FrameworkException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.op_ra.enums.LogType.ERROR;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;

public final class DataBaseConnectionUtils {
    private static final String LOG_TAG = DataBaseConnectionUtils.class.getSimpleName();
    private static final String runmode = PropertyUtils.getValue(ConfigProperties.RUN_MODE);
    private static Connection myConn;
//    private static final String runmode = "local";

    static {
        try {
            initializeConnection();
        } catch (Exception e) {
            log(ERROR, LOG_TAG + ": Error initializing database connection.");
            throw new RuntimeException(e);
        }
    }

    private DataBaseConnectionUtils() {
    }

    private static void initializeConnection() throws SQLException, ClassNotFoundException {
        switch (runmode.toLowerCase()) {
            case "local":
                System.out.println("Local MYSQL");
                connectToMySQL();
                break;
            case "remote":
                connectToSQLite();
                break;
            default:
                throw new IllegalArgumentException("Invalid run mode: " + runmode);
        }
    }

    private static void connectToMySQL() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String hostName = JsonConfigUtils.get(DataBaseProperties.HOSTNAME);
        String port = JsonConfigUtils.get(DataBaseProperties.PORT);
        String schema = JsonConfigUtils.get(DataBaseProperties.SCHEMA);
        String dbusername = JsonConfigUtils.get(DataBaseProperties.DBUSERNAME);
        String password = JsonConfigUtils.get(DataBaseProperties.DBPASSWORD);
        log(INFO, LOG_TAG + ": Connected to MySQL database successfully." + hostName + port + schema + dbusername + password);

        //String url = "jdbc:mysql://" + hostName + ":" + port + "/" + schema + "?autoReconnect=true&useSSL=false";
        String url = "jdbc:mysql://" + hostName + ":" + port + "/" + schema;
        log(INFO, LOG_TAG + ": Connected to MySQL database successfully." + url);
        try {
            myConn = DriverManager.getConnection(url, dbusername, password);
            log(INFO, LOG_TAG + ": Connected to MySQL database successfully.");
        } catch (SQLException e) {
            log(ERROR, LOG_TAG + ": Error connecting to MySQL database.");
            throw new SQLException("Error connecting to MySQL database.", e);
        }
    }

    private static void connectToSQLite() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String databasePath = FrameworkConstants.getDatabasePath();
        String url = "jdbc:sqlite:" + databasePath;

        try {
            myConn = DriverManager.getConnection(url);
            log(INFO, LOG_TAG + ": Connected to SQLite database successfully.");
        } catch (SQLException e) {
            log(ERROR, LOG_TAG + ": Error connecting to SQLite database.");
            throw new SQLException("Error connecting to SQLite database.", e);
        }
    }

    public static Connection getMyConn() {
        try {
            if (myConn == null || myConn.isClosed()) {
                initializeConnection();
            }
        } catch (SQLException | ClassNotFoundException e) {
            log(ERROR, LOG_TAG + ": Error obtaining database connection.");
            e.printStackTrace();
            return null;
        }
        return myConn;
    }

    public static void closeConnection() {
        if (myConn != null) {
            try {
                myConn.close();
                log(INFO, LOG_TAG + ": Database connection closed.");
            } catch (SQLException e) {
                log(ERROR, LOG_TAG + ": Error closing database connection.");
                throw new FrameworkException("Error closing database connection", e);
            }
        }
    }
}