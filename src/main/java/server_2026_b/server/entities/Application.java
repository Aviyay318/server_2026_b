package server_2026_b.server.entities;

import server_2026_b.server.utils.AbsenceReason;
import server_2026_b.server.utils.ApplicationStatus;

import java.sql.Timestamp;

public class Application {

    private Long id;
    private Long employeeId;
    private Long employerId;
    private Timestamp date;
    private AbsenceReason reason;
    private ApplicationStatus status;

    public Application() {
    }

    public Application(Long employeeId, Long employerId, Timestamp date,
                       AbsenceReason reason, ApplicationStatus status) {
        this.employeeId = employeeId;
        this.employerId = employerId;
        this.date = date;
        this.reason = reason;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

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

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
