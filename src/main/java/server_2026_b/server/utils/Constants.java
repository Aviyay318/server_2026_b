package server_2026_b.server.utils;

public class Constants {
    public static final String SCHEMA = "server_2026_b";
    public static final String DB_HOST = "localhost";
    public static final int DB_PORT = 3306;
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "1234";










    // JWT TOKEN DO NOT CHANGE
    public static final String JWT_SECRET = "DevOnlySecretIfThisReachesProductionPleasePanicAndReplaceIt12345";
    public static final long JWT_ACCESS_EXPIRATION = 1000L * 60 * 15;
    public static final long JWT_REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7;
    public static final String USER_ID = "userId";
    public static final String USER_TYPE = "userType";
    public static final String TOKEN_TYPE = "tokenType";
    public static final String ACCESS_TOKEN = "ACCESS";
    public static final String REFRESH_TOKEN = "REFRESH";

    public static final int TOKEN_VALID_HOURS = 24;

    private Constants() {
    }
}
