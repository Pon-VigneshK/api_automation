package org.op_ra.enums;

/**
 * Enumeration for database related configuration property keys.
 * This enum can be used to specifically fetch database configuration
 * if they are grouped or need to be distinctly identified from other {@link ConfigProperties}.
 * <p>
 * Currently, these might overlap with {@link ConfigProperties}. This enum can be used
 * if a more granular approach to fetching DB properties is desired or if they are stored
 * in a separate properties file in the future.
 * </p>
 * @deprecated Consider merging these into {@link ConfigProperties} if they are sourced from the same file
 *             and there is no specific need for a separate enum for DB properties.
 *             If used, ensure {@link org.op_ra.utils.PropertyUtils} can handle them or a specific
 *             utility is created for these.
 */
@Deprecated
public enum DataBaseProperties {
    /** JDBC URL for the database connection. */
    DB_URL,
    /** Username for the database connection. */
    DB_USERNAME,
    /** Password for the database connection. */
    DB_PASSWORD,
    /** Database name, if needed separately from the URL. */
    DB_NAME,
    /** Database host address. */
    DB_HOST,
    /** Database port number. */
    DB_PORT
}
