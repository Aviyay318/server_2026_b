package server_2026_b.server.responses;

import server_2026_b.server.utils.UserType;

public class LoginResponse extends BasicResponse {
    private String message;
    private String token;
    private UserType userType;

    public LoginResponse(Integer errorCode, boolean success, String message, String token, UserType userType) {
        super(success, errorCode);
        this.message = message;
        this.token = token;
        this.userType = userType;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public UserType getUserType() {
        return userType;
    }
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}