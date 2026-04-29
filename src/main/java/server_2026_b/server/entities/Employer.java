package server_2026_b.server.entities;

public class Employer {

    private Long id;
    private String username;
    private String password;
    private String businessName;
    private String phone;
    //private String email;

    public Employer() {
    }

    public Employer(String phone, String businessName, String password, String username) {
        this.phone = phone;
        this.businessName = businessName;
        this.password = password;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}