package server_2026_b.server.requests;

import server_2026_b.server.utils.AbsenceReason;

public class ReportAbsenceRequest {
    private AbsenceReason reason;

    public AbsenceReason getReason() {
        return reason;
    }

    public void setReason(AbsenceReason reason) {
        this.reason = reason;
    }
}
