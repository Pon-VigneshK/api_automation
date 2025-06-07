package org.op_ra.exceptions;

/**
 * Custom exception to indicate that an invalid or incorrect path has been specified for a generic file.
 * This can be used when file path issues are encountered for files other than Excel,
 * such as configuration files, JSON templates, or report files.
 */
public class InvalidPathForFilesException extends FrameworkException {

    /**
     * Constructs a new InvalidPathForFilesException with the specified detail message.
     *
     * @param message the detail message.
     */
    public InvalidPathForFilesException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidPathForFilesException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public InvalidPathForFilesException(String message, Throwable cause) {
        super(message, cause);
    }
}
