package server_2026_b.server.responses;

import server_2026_b.server.entities.User;
import java.util.List;

public class EmployeeListResponse extends BasicResponse {

    private List<UserDTO> employees;

    public EmployeeListResponse(boolean success, List<UserDTO> employees) {
        super(success, null);
        this.employees = employees;
    }

    public EmployeeListResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public List<UserDTO> getEmployees() { return employees; }
    public void setEmployees(List<UserDTO> employees) { this.employees = employees; }
}
