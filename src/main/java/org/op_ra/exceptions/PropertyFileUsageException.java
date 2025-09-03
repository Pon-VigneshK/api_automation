package org.op_ra.exceptions;

import org.op_ra.constants.FrameworkConstants;
import org.op_ra.utils.PropertyUtils;

/**
 * A runtime exception occurs when the key or value fetched from the property file is null.
 *
 * @see FrameworkConstants
 * @see PropertyUtils
 */
@SuppressWarnings("serial")
public class PropertyFileUsageException extends FrameworkException {

    /**
     * Pass the message that needs to be appended to the stacktrace
     *
     * @param message Details about the exception or custom message
     */
    public PropertyFileUsageException(String message) {
        super(message);
    }

    /**
     * @param message Details about the exception or custom message
     * @param cause   Pass the enriched stacktrace or customised stacktrace
     */
    public PropertyFileUsageException(String message, Throwable cause) {
        super(message, cause);
    }

}
