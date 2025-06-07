package org.op_ra.exceptions;

/**
 * A generic custom exception for the testing framework.
 * This class serves as a base exception for more specific framework-related issues
 * or can be used directly for general framework errors.
 * <p>
 * Using custom exceptions helps in identifying and handling errors originating
 * specifically from the test automation framework.
 * </p>
 */
public class FrameworkException extends RuntimeException {

    /**
     * Constructs a new framework exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public FrameworkException() {
        super();
    }

    /**
     * Constructs a new framework exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public FrameworkException(String message) {
        super(message);
    }

    /**
     * Constructs a new framework exception with the specified detail message and cause.
     * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
     * incorporated in this runtime exception'''s detail message.</p>
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new framework exception with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for runtime exceptions that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public FrameworkException(Throwable cause) {
        super(cause);
    }
}
