package jks.lototronback.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BytesConverter {

    public static byte[] stringToBytesArray(String value) {
        if (value == null || value.isEmpty()) {
            return new byte[0];
        }

        if (value.startsWith("data:image")) {
            String base64Data = value.substring(value.indexOf(',') + 1);
            return Base64.getDecoder().decode(base64Data);
        }

        return value.getBytes(StandardCharsets.UTF_8);
    }

    public static String bytesArrayToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }

        String base64 = Base64.getEncoder().encodeToString(bytes);
        return "data:image/jpeg;base64," + base64;
    }
}