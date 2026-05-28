package server_2026_b.server.requests;

import server_2026_b.server.utils.AbsenceReason;

import java.sql.Timestamp;
import java.util.Date;

public class ReportAbsenceRequest {
    private Timestamp date;
    private AbsenceReason reason;

    public AbsenceReason getReason() {
        return reason;
    }

    public void setReason(AbsenceReason reason) {
        this.reason = reason;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
