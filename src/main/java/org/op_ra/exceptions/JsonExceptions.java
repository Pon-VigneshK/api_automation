package org.op_ra.exceptions;

@SuppressWarnings("serial")
public class JsonExceptions extends FrameworkException {
    public JsonExceptions(String message) {
        super(message);
    }

    public JsonExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}