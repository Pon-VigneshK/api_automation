package org.op_ra.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * {@code StringDecoder} is a utility class that provides a method for decoding Base64 encoded strings.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The {@code decodeBase64UTF8} method can be called to decode a Base64 encoded string.</p>
 *
 * <pre>{@code
 * String decodedString = StringDecoder.decodeBase64UTF8("SGVsbG8gV29ybGQ=");
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class StringDecoder {

    /**
     * Decodes a Base64 encoded string using the UTF-8 charset.
     *
     * @param encoded The Base64 encoded string to decode.
     * @return The decoded string, or {@code null} if the input is null.
     */
    public static String decodeBase64UTF8(String encoded) {
        if (encoded == null) {
            return null;
        }
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
