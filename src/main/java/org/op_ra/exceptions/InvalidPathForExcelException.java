package org.op_ra.exceptions;

/**
 * {@code InvalidPathForExcelException} is a runtime exception that is thrown when an invalid
 * file path is provided for an Excel file. This exception extends {@link InvalidPathForFilesException}
 * and is used to indicate a specific type of file path error related to Excel files.
 *
 * <p>This exception is typically thrown when the framework attempts to read data from an Excel
 * file, but the specified path does not exist or is incorrect.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>This exception can be thrown with a custom message or with a message and a cause.</p>
 *
 * <pre>{@code
 * // Throwing with a custom message
 * if (!new File(filePath).exists()) {
 *     throw new InvalidPathForExcelException("The specified Excel file path does not exist: " + filePath);
 * }
 *
 * // Throwing with a message and a cause
 * try {
 *     // Attempt to read the Excel file
 * } catch (IOException e) {
 *     throw new InvalidPathForExcelException("Failed to read the Excel file.", e);
 * }
 * }</pre>
 *
 * @see InvalidPathForFilesException
 * @version 1.0
 * @since 2023-10-27
 */
@SuppressWarnings("serial")
public class InvalidPathForExcelException extends InvalidPathForFilesException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public InvalidPathForExcelException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause The cause of the exception, which is saved for later retrieval by the {@link #getCause()} method.
     *              A {@code null} value is permitted and indicates that the cause is nonexistent or unknown.
     */
    public InvalidPathForExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
