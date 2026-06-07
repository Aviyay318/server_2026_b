package server_2026_b.server.responses;

import java.sql.Timestamp;

public class EmployeeConstraintResponse { // שימוש רק במחלקה של EmployeeConstraintsResponse כדי להחזיר אובייקט אילוץ ללא id אוטומטי

    private String employeeId;
    private long shiftId;
    private boolean available;
    private String comment;
    private Timestamp date;

    public EmployeeConstraintResponse() {}

    public EmployeeConstraintResponse(String employeeId, long shiftId, boolean available, String comment, Timestamp date) {
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
