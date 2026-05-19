package uz.uptimehub.core.utils;


import java.util.Arrays;

public class AuthHeaderUtils {

    private AuthHeaderUtils() {
    }

    public static String[] extractPermissions(String header) {

        if (header == null || header.isEmpty()) {
            return new String[0];
        }

        return Arrays.stream(header.split(","))
                .map(String::trim)
                .filter(permission -> !permission.isBlank())
                .toArray(String[]::new);
    }
}
