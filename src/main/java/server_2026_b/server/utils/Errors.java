package server_2026_b.server.utils;

public class Errors {
    public static final int ERROR_INVALID_TOKEN = 2000;

    public static final int  ERROR_EMPTY_FIELD = 3000;
    public static final int ERROR_WRONG_CREDENTIALS = 3001;
    public static final int ERROR_UPDATE_TOKEN_FAILED = 3002;
    public static final int ERROR_INVALID_ID = 3003;

    public static final int ERROR_EMPLOYEE_ALREADY_WORKING = 4000;
    public static final int ERROR_EMPLOYEE_NOT_WORKING = 4001;
    public static final int ERROR_INVALID_ABSENCE_REASON = 4002;
    public static final int ERROR_NO_EMPLOYER_FOUND = 4003;
    public static final int ERROR_APPLICATION_NOT_FOUND = 4004;
    public static final int ERROR_APPLICATION_NOT_YOURS = 4005;
    public static final int ERROR_APPLICATION_ALREADY_ANSWERED = 4006;

    public static final int ERROR_FETCHING_SITES = 5001;
    public static final int ERROR_SITE_NOT_FOUND = 5002;
    public static final int ERROR_TWO_LOCATIONS_AT_ONCE = 5003;
    public static final int ERROR_NO_LOCATION_OR_SITE_PROVIDED = 5004;

    public static final int ERROR_EMPLOYEE_ALREADY_EXISTS = 6001;
    public static final int ERROR_EMPLOYEE_NOT_FOUND = 6002;
    public static final int ERROR_NOT_YOUR_EMPLOYEE = 6003;
    public static final int ERROR_CANNOT_DELETE_EMPLOYER = 6004;

    public static final int ERROR_EMPLOYER_ALREADY_EXISTS = 6005;
    public static final int ERROR_EMPLOYER_NOT_FOUND = 6006;


    public static final int ERROR_FETCHING_SHIFTS = 7001;
    public static final int ERROR_SHIFT_NOT_FOUND = 7002;
    public static final int ERROR_INVALID_DATE_FORMAT = 7003;


    public static final int ERROR_TEST_ADD_DATA_FAILED = 9001;
}