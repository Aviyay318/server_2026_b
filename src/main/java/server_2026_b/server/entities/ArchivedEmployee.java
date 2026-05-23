package server_2026_b.server.entities;

import server_2026_b.server.utils.UserType;
import java.time.LocalDateTime;

public class ArchivedEmployee {

    private Long id;
    private String personalId;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private String email;
    private UserType userType;
    private LocalDateTime deletedAt;
    private Long deletedByEmployerId;

    public ArchivedEmployee(Long id, String personalId, String firstName, String lastName, String password, String phone, String email, UserType userType, LocalDateTime deletedAt, Long deletedByEmployerId) {
        this.id = id;
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.userType = userType;
        this.deletedAt = deletedAt;
        this.deletedByEmployerId = deletedByEmployerId;
    }

    public ArchivedEmployee(){}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPersonalId() { return personalId; }
    public void setPersonalId(String personalId) { this.personalId = personalId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    public Long getDeletedByEmployerId() { return deletedByEmployerId; }
    public void setDeletedByEmployerId(Long deletedByEmployerId) { this.deletedByEmployerId = deletedByEmployerId; }
}
