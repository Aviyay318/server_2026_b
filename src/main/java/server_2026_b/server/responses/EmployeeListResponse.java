package server_2026_b.server.responses;

import server_2026_b.server.entities.Employee;
import java.util.List;

public class EmployeeListResponse extends BasicResponse {

    private List<Employee> employees;

    public EmployeeListResponse(boolean success, List<Employee> employees) {
        super(success, null);
        this.employees = employees;
    }

    public EmployeeListResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}
