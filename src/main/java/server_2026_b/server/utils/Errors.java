package server_2026_b.server.utils;

public class Errors {
    public static final int ERROR_INVALID_TOKEN = 2000;

    public static final int  ERROR_EMPTY_FIELD = 3000;
    public static final int ERROR_WRONG_CREDENTIALS = 3001;
    public static final int ERROR_UPDATE_TOKEN_FAILED = 3002;

    public static final int ERROR_EMPLOYEE_ALREADY_WORKING = 4000;
    public static final int ERROR_EMPLOYEE_NOT_WORKING = 4001;

    public static final int ERROR_FETCHING_SITES = 5001;
    public static final int ERROR_SITE_NOT_FOUND = 5002;
    public static final int ERROR_TWO_LOCATIONS_AT_ONCE = 5003;
    public static final int ERROR_NO_LOCATION_OR_SITE_PROVIDED = 5004;
}