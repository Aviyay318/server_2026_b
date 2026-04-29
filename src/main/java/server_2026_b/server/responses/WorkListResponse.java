package server_2026_b.server.responses;

import server_2026_b.server.entities.WorkDay;

import java.util.List;

public class WorkListResponse extends BasicResponse{
    private List<WorkDay> workDays;

    public WorkListResponse(boolean success, Integer errorCode, List<WorkDay> workDays) {
        super(success, errorCode);
        this.workDays = workDays;
    }

    public List<WorkDay> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(List<WorkDay> workDays) {
        this.workDays = workDays;
    }
}
