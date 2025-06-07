package org.op_ra.exceptions;

/**
 * Custom exception related to errors encountered while using property files.
 * This can include issues like the property file not being found, a required property key missing,
 * or errors during loading of properties.
 */
public class PropertyFileUsageException extends FrameworkException {

    /**
     * Constructs a new PropertyFileUsageException with the specified detail message.
     *
     * @param message the detail message.
     */
    public PropertyFileUsageException(String message) {
        super(message);
    }

    /**
     * Constructs a new PropertyFileUsageException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception (e.g., an underlying IOException).
     */
    public PropertyFileUsageException(String message, Throwable cause) {
        super(message, cause);
    }
}
