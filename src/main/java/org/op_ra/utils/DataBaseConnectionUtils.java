package org.op_ra.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.op_ra.constants.FrameworkConstants;
import org.op_ra.enums.CloudDataBaseProperties;
import org.op_ra.enums.ConfigProperties;
import org.op_ra.exceptions.FrameworkException;
import org.op_ra.exceptions.JsonExceptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.op_ra.enums.LogType.ERROR;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;

public final class DataBaseConnectionUtils {

    private static Map<String, String> CONFIGMAP;

    static {
        try {
            CONFIGMAP = new ObjectMapper().readValue(
                    new File(FrameworkConstants.getCloudDbConfigJsonFilePath()),
                    new TypeReference<HashMap<String, String>>() {
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(CloudDataBaseProperties key) throws JsonExceptions {
        if (Objects.isNull(key)) {
            throw new JsonExceptions("Key is null. Please provide a valid key.");
        }
        String value = CONFIGMAP.get(key.name().toLowerCase());
        if (Objects.isNull(value)) {
            throw new JsonExceptions("Property name " + key + " is not found in the JSON file. Please check the DataBaseConfig.json file.");
        }
        return value;
    }

    private static final String LOG_TAG = DataBaseConnectionUtils.class.getSimpleName();
    private static final String runmode = PropertyUtils.getValue(ConfigProperties.RUNMODE);
    private static Connection myConn;

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
            case "remote":
                connectToMySQL();
                break;
            case "local":
                connectToSQLite();
                break;
            default:
                throw new IllegalArgumentException("Invalid run mode: " + runmode);
        }
    }

    private static Connection connectToMySQL() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String hostName = get(CloudDataBaseProperties.HOSTNAME);
        String port = get(CloudDataBaseProperties.PORT);
        String schema = get(CloudDataBaseProperties.SCHEMA);
        // Get the Base64 encoded values from config and decode them
        String encodedUsername = get(CloudDataBaseProperties.DBUSERNAME);
        String encodedPassword = get(CloudDataBaseProperties.DBPASSWORD);
        String dbusername = StringDecoder.decodeBase64UTF8(encodedUsername);
        String password = StringDecoder.decodeBase64UTF8(encodedPassword);
        log(INFO, LOG_TAG + ": Connected to MySQL database successfully." + hostName + port + schema + dbusername + password);
        String url = "jdbc:mysql://" + hostName + ":" + port + "/" + schema;
        log(INFO, LOG_TAG + ": Connected to MySQL database successfully." + url);
        try {
            myConn = DriverManager.getConnection(url, dbusername, password);
            log(INFO, LOG_TAG + ": Connected to MySQL database successfully.");
        } catch (SQLException e) {
            log(ERROR, LOG_TAG + ": Error connecting to MySQL database.");
            throw new SQLException("Error connecting to MySQL database.", e);
        }
        return myConn;
    }

    private static Connection connectToSQLite() throws SQLException, ClassNotFoundException {
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
        return myConn;
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

    public static Connection getCloudConn() {
        try {
            if (myConn == null || myConn.isClosed()) {
                connectToMySQL();
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

    public static Map<String, String> retrieveRowDataSqLite(String query) {
        Map<String, String> row = new HashMap<>();
        try (Connection connection = connectToSQLite();
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