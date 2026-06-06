package server_2026_b.server.responses;

import server_2026_b.server.entities.WorkShift;
import java.util.List;

public class WorkShiftResponse extends BasicResponse {
    private List<WorkShift> workShifts;

    public WorkShiftResponse(boolean success, Integer errorCode, List<WorkShift> workShifts) {
        super(success, errorCode);
        this.workShifts = workShifts;
    }

    public List<WorkShift> getWorkShifts() {
        return workShifts;
    }

    public void setWorkShifts(List<WorkShift> workShifts) {
        this.workShifts = workShifts;
    }
    
}