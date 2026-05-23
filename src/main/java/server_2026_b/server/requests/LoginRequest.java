package server_2026_b.server.requests;

public class LoginRequest {
    private String personalId;
    private String password;

    public LoginRequest() {
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
