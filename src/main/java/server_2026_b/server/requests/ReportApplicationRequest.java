package server_2026_b.server.requests;

import server_2026_b.server.utils.AbsenceReason;

import java.sql.Timestamp;

public class ReportApplicationRequest {
    private Timestamp date;
    private AbsenceReason reason;

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public AbsenceReason getReason() {
        return reason;
    }

    public void setReason(AbsenceReason reason) {
        this.reason = reason;
    }
}
