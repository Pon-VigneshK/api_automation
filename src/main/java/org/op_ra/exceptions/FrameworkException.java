package org.op_ra.exceptions;

/**
 * BaseException for the framework. Extends Runtime Exception and can be thrown anywhere to terminate a program
 */
@SuppressWarnings("serial")
public class FrameworkException extends RuntimeException {

    /**
     * Pass the message that needs to be appended to the stacktrace
     *
     * @param message Details about the exception or custom message
     */
    public FrameworkException(String message) {
        super(message);
    }

    /**
     * @param message Details about the exception or custom message
     * @param cause   Pass the enriched stacktrace or customised stacktrace
     */
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

}
