package server_2026_b.server.responses;

import server_2026_b.server.utils.AbsenceReason;

import java.util.List;

public class AbsenceReasonsResponse extends BasicResponse {
    private List<AbsenceReason> absenceReasons;

    public AbsenceReasonsResponse(boolean success, Integer errorCode, List<AbsenceReason> absenceReasons) {
        super(success, errorCode);
        this.absenceReasons = absenceReasons;
    }

    public List<AbsenceReason> getAbsenceReasons() {
        return absenceReasons;
    }

    public void setAbsenceReasons(List<AbsenceReason> absenceReasons) {
        this.absenceReasons = absenceReasons;
    }
}
