package server_2026_b.server.responses;

import server_2026_b.server.entities.WorkDay;

import java.util.List;

public class EmployeeResponse extends BasicResponse{
    private UserDTO employee;
    private List<WorkDay> workDayList;

    public EmployeeResponse(boolean success, Integer errorCode, UserDTO employee, List<WorkDay> workDayList) {
        super(success, errorCode);
        this.employee = employee;
        this.workDayList = workDayList;
    }

    public List<WorkDay> getWorkDayList() {
        return workDayList;
    }

    public void setWorkDayList(List<WorkDay> workDayList) {
        this.workDayList = workDayList;
    }

    public EmployeeResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public UserDTO getEmployee() {
        return employee;
    }

    public void setEmployee(UserDTO employee) {
        this.employee = employee;
    }
}
