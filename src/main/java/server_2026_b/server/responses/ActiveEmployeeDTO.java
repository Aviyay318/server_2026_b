package server_2026_b.server.responses;

import java.sql.Timestamp;

public class ActiveEmployeeDTO {
    private UserDTO employee;
    private Timestamp startTime;
    private String location;

    public ActiveEmployeeDTO(UserDTO employee, Timestamp startTime, String location) {
        this.employee = employee;
        this.startTime = startTime;
        this.location = location;
    }

    public UserDTO getEmployee() {
        return employee;
    }

    public void setEmployee(UserDTO employee) {
        this.employee = employee;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
