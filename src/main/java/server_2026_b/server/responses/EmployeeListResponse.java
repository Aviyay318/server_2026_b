package server_2026_b.server.responses;

import server_2026_b.server.entities.User;
import java.util.List;

public class EmployeeListResponse extends BasicResponse {

    private List<User> employees;

    public EmployeeListResponse(boolean success, List<User> employees) {
        super(success, null);
        this.employees = employees;
    }

    public EmployeeListResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public List<User> getEmployees() { return employees; }
    public void setEmployees(List<User> employees) { this.employees = employees; }
}
