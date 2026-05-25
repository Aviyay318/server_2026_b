package server_2026_b.server.requests;

import server_2026_b.server.utils.AbsenceReason;
import java.util.Date;

public class ReportAbsenceRequest {
    private Date date;
    private AbsenceReason reason;

    public AbsenceReason getReason() {
        return reason;
    }

    public void setReason(AbsenceReason reason) {
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
