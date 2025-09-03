package org.op_ra.exceptions;

/**
 * {@code InvalidPathForFilesException} is a runtime exception that is thrown when an invalid
 * file path is provided for a file. This exception extends {@link FrameworkException} and serves
 * as a base class for more specific file path exceptions, such as {@link InvalidPathForExcelException}.
 *
 * <p>This exception is typically thrown when a file path is found to be null, empty, or does not
 * point to a valid file, and the specific file type is not covered by a more specific exception.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>This exception can be thrown with a custom message or with a message and a cause.</p>
 *
 * <pre>{@code
 * // Throwing with a custom message
 * if (filePath == null || filePath.trim().isEmpty()) {
 *     throw new InvalidPathForFilesException("The file path cannot be null or empty.");
 * }
 *
 * // Throwing with a message and a cause
 * try {
 *     // Attempt to access a file
 * } catch (Exception e) {
 *     throw new InvalidPathForFilesException("Failed to access the file at path: " + filePath, e);
 * }
 * }</pre>
 *
 * @see FrameworkException
 * @see InvalidPathForExcelException
 * @version 1.0
 * @since 2023-10-27
 */
@SuppressWarnings("serial")
public class InvalidPathForFilesException extends FrameworkException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public InvalidPathForFilesException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause The cause of the exception, which is saved for later retrieval by the {@link #getCause()} method.
     *              A {@code null} value is permitted and indicates that the cause is nonexistent or unknown.
     */
    public InvalidPathForFilesException(String message, Throwable cause) {
        super(message, cause);
    }
}
