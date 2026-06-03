package server_2026_b.server.entities;

import java.sql.Timestamp;

public class EmployeeConstraint {
    private long id; // השארתי למקרה שיהיה שימוש בעתיד, יש EmployeeConstraintResponse שמחזיר אובייקט יחיד בלי זה
    private String employeeId;
    private long shiftId;
    private boolean available;
    private String comment; // גם אם יכול וגם אם לא יכול, לדוגמה: יכול רק עד 12:00
    private Timestamp date;

    public EmployeeConstraint() {}

    public EmployeeConstraint(String employeeId, long shiftId, boolean available, String comment, Timestamp date) {
        this.employeeId = employeeId;
        this.shiftId = shiftId;
        this.available = available;
        this.comment = comment;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
