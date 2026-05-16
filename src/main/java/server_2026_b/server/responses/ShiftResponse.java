package server_2026_b.server.responses;
import server_2026_b.server.entities.Shift;
import java.util.List;


public class ShiftResponse extends BasicResponse {
    private List<Shift> shifts;

    public ShiftResponse(boolean success, Integer errorCode,List<Shift> shifts) {
        super(success, errorCode);
        this.shifts = shifts;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }
    
}
