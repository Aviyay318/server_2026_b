package server_2026_b.server.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    private CookieUtils() {
    }

    public static void setAuthCookies(HttpServletResponse response,
                                      String accessToken,
                                      String refreshToken) {
        addCookie(response, Constants.ACCESS_TOKEN_COOKIE, accessToken,
                (int) (Constants.JWT_ACCESS_EXPIRATION / 1000), "/");

        addCookie(response, Constants.REFRESH_TOKEN_COOKIE, refreshToken,
                (int) (Constants.JWT_REFRESH_EXPIRATION / 1000), "/auth");
    }

    public static void clearAuthCookies(HttpServletResponse response) {
        addCookie(response, Constants.ACCESS_TOKEN_COOKIE, "", 0, "/");
        addCookie(response, Constants.REFRESH_TOKEN_COOKIE, "", 0, "/auth");
    }

    private static void addCookie(HttpServletResponse response,
                                  String name,
                                  String value,
                                  int maxAgeSeconds,
                                  String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath(path);
        cookie.setMaxAge(maxAgeSeconds);
        response.addCookie(cookie);
    }

    public static void setAccessCookie(HttpServletResponse response, String accessToken) {
        addCookie(response, Constants.ACCESS_TOKEN_COOKIE, accessToken,
                (int) (Constants.JWT_ACCESS_EXPIRATION / 1000), "/");
    }
}
