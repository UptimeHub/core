package uz.uptimehub.coreapp.utils;

import jakarta.servlet.http.HttpServletRequest;

public class CustomHeaders {

    private static final String PERMISSIONS_HEADER = "X-Auth-Permissions";

    public static String[] extractPermissions(HttpServletRequest request) {
        String header = request.getHeader(PERMISSIONS_HEADER);

        if (header == null) {
            return new String[0];
        }

        return header.split(",");
    }
}
