package org.op_ra.exceptions;

/**
 * Custom exception to indicate that an invalid or incorrect path has been specified for an Excel file.
 * This exception is typically thrown when the framework attempts to access an Excel file
 * (e.g., for test data or runner lists) at a path that does not exist or is not accessible.
 */
public class InvalidPathForExcelException extends FrameworkException {

    /**
     * Constructs a new InvalidPathForExcelException with the specified detail message.
     *
     * @param message the detail message.
     */
    public InvalidPathForExcelException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidPathForExcelException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public InvalidPathForExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
