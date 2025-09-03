package org.op_ra.exceptions;

/**
 * {@code FrameworkException} is the base exception for the testing framework. It extends
 * {@link RuntimeException}, making it an unchecked exception that can be thrown anywhere
 * in the framework to terminate a program and indicate a critical error.
 *
 * <p>This exception is used as a generic exception for framework-related issues that are not
 * covered by more specific custom exceptions. It can be used to wrap other exceptions to provide
 * more context about the error.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>This exception can be thrown with a custom message or with a message and a cause.</p>
 *
 * <pre>{@code
 * // Throwing with a custom message
 * throw new FrameworkException("A critical framework error occurred.");
 *
 * // Throwing with a message and a cause
 * try {
 *     // Some operation that might throw an exception
 * } catch (Exception e) {
 *     throw new FrameworkException("Failed to perform the operation.", e);
 * }
 * }</pre>
 *
 * @see java.lang.RuntimeException
 * @version 1.0
 * @since 2023-10-27
 */
@SuppressWarnings("serial")
public class FrameworkException extends RuntimeException {

    /**
     * Constructs a new framework exception with the specified detail message.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public FrameworkException(String message) {
        super(message);
    }

    /**
     * Constructs a new framework exception with the specified detail message and cause.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause The cause of the exception, which is saved for later retrieval by the {@link #getCause()} method.
     *              A {@code null} value is permitted and indicates that the cause is nonexistent or unknown.
     */
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

}
