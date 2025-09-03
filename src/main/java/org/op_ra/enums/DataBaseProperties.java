package org.op_ra.enums;

/**
 * {@code DataBaseProperties} is an enumeration that defines the keys for local database configuration.
 * This enum is used to retrieve database connection details from a configuration source,
 * such as a properties file or a JSON file. Using this enum ensures that the keys are consistent
 * and avoids errors due to typos.
 *
 * <p><b>Usage:</b></p>
 * <p>This enum is used with the {@link org.op_ra.utils.JsonConfigUtils} to get the values for the database configuration.</p>
 *
 * <pre>{@code
 * String hostname = JsonConfigUtils.get(DataBaseProperties.HOSTNAME);
 * String port = JsonConfigUtils.get(DataBaseProperties.PORT);
 * }</pre>
 *
 * <p>The available properties are:</p>
 * <ul>
 *   <li>{@link #HOSTNAME} - The hostname of the database server.</li>
 *   <li>{@link #PORT} - The port number of the database server.</li>
 *   <li>{@link #SCHEMA} - The name of the database schema.</li>
 *   <li>{@link #DBUSERNAME} - The username for connecting to the database.</li>
 *   <li>{@link #DBPASSWORD} - The password for connecting to the database.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public enum DataBaseProperties {

    /**
     * The hostname of the database server (e.g., "localhost", "127.0.0.1").
     */
    HOSTNAME,

    /**
     * The port number on which the database server is listening (e.g., "3306" for MySQL).
     */
    PORT,

    /**
     * The name of the database schema to connect to.
     */
    SCHEMA,

    /**
     * The username used to authenticate with the database.
     */
    DBUSERNAME,

    /**
     * The password used to authenticate with the database.
     */
    DBPASSWORD

}
