package server_2026_b.server.requests;

import java.sql.Timestamp;

public class EmployeeDateRequest {
    private Timestamp date;

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
