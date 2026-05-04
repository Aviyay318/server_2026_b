package server_2026_b.server.utils;

public class ConfigurationErrors {
    public static final String JWT_SECRET_MISSING = "JWT_SECRET is missing";
    public static final String JWT_ACCESS_EXPIRATION_MUST_BE_POSITIVE = "JWT_ACCESS_EXPIRATION must be positive";
    public static final String JWT_REFRESH_EXPIRATION_MUST_BE_POSITIVE = "JWT_REFRESH_EXPIRATION must be positive";
    public static final String JWT_SECRET_TOO_SHORT = "JWT_SECRET must be at least 32 characters long";
    public static final String JWT_INVALID_EXPIRATION_TIME = "JWT_REFRESH_EXPIRATION must be greater than JWT_ACCESS_EXPIRATION";
}
