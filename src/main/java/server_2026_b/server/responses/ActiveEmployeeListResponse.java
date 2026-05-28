package server_2026_b.server.responses;

import java.util.List;

public class ActiveEmployeeListResponse extends BasicResponse {
    private List<ActiveEmployeeDTO> employees;

    public ActiveEmployeeListResponse(boolean success, List<ActiveEmployeeDTO> employees) {
        super(success, null);
        this.employees = employees;
    }

    public ActiveEmployeeListResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public List<ActiveEmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<ActiveEmployeeDTO> employees) {
        this.employees = employees;
    }
}
