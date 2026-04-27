package server_2026_b.server.responses;

public class ResponseDto extends BasicResponse {
    private String message;
    private String token;
    private String userType;

    public ResponseDto(Integer errorCode,boolean success,String message, String token, String userType) {
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
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
}
