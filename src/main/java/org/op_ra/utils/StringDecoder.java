package org.op_ra.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StringDecoder {

    public static String decodeBase64UTF8(String encoded) {
        if (encoded == null) {
            return null;
        }
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }


}
