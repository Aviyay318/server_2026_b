package server_2026_b.server.requests;

import java.sql.Timestamp;

public class EmployeeConstraintRequest {
    // ללא auto-increment id
    private String employeeId;  // הבדיקה של הת״ז תתבצע אל מול הטוקן ב-employeeConstraintsService
    private long shiftId;
    private boolean available;
    private String comment;
    private Timestamp date;

    public EmployeeConstraintRequest() {}

    public EmployeeConstraintRequest(String employeeId, long shiftId, boolean available, String comment, Timestamp date) {
        this.employeeId = employeeId;
        this.shiftId = shiftId;
        this.available = available;
        this.comment = comment;
        this.date = date;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public long getShiftId() {
        return shiftId;
    }

    public void setShiftId(long shiftId) {
        this.shiftId = shiftId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
