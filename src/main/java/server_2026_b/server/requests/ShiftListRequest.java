package server_2026_b.server.requests;

import java.util.List;

public class ShiftListRequest {

    private List<ShiftRequest> shifts;

    public ShiftListRequest() {
    }

    public List<ShiftRequest> getShifts() {
        return shifts;
    }

    public void setShifts(List<ShiftRequest> shifts) {
        this.shifts = shifts;
    }
}