package server_2026_b.server.entities;

import java.sql.Timestamp;

public class EmployeeConstraint {
    private long id;
    private User employee;
    private Shift shift;
    private boolean available;
    private String comment;
    private Timestamp date;

    public EmployeeConstraint() {}

    public EmployeeConstraint(User employee, Shift shift, boolean available, String comment, Timestamp date) {
        this.employee = employee;
        this.shift = shift;
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

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
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
