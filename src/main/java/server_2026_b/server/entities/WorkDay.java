package server_2026_b.server.entities;

import server_2026_b.server.utils.AbsenceReason;
import server_2026_b.server.utils.ShiftStatus;

import java.sql.Timestamp;

public class WorkDay {

    private Long id;
    private Long userId; // id של עובד
    private Timestamp enterTime;
    private Timestamp exitTime;
    private String enterLocation;
    private String exitLocation;
    private WorkingSite enterSite;
    private WorkingSite exitSite;
    private ShiftStatus status;
    private AbsenceReason absenceReason;

    public WorkDay() {
    }

    public WorkDay(Long id, Long userId, Timestamp enterTime, Timestamp exitTime,
                   String enterLocation, String exitLocation,
                   WorkingSite enterSite, WorkingSite exitSite) {
        this.id = id;
        this.userId = userId;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.enterLocation = enterLocation;
        this.exitLocation = exitLocation;
        this.enterSite = enterSite;
        this.exitSite = exitSite;
    }

    public WorkDay(Long userId, Timestamp enterTime, Timestamp exitTime,
                   String enterLocation, String exitLocation,
                   WorkingSite enterSite, WorkingSite exitSite) {
        this.userId = userId;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.enterLocation = enterLocation;
        this.exitLocation = exitLocation;
        this.enterSite = enterSite;
        this.exitSite = exitSite;
    }

    public WorkDay(Long userId, Timestamp enterTime, WorkingSite enterSite, String enterLocation) {
        this.userId = userId;
        this.enterTime = enterTime;
        this.enterSite = enterSite;
        this.enterLocation = enterLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Timestamp enterTime) {
        this.enterTime = enterTime;
    }

    public Timestamp getExitTime() {
        return exitTime;
    }

    public void setExitTime(Timestamp exitTime) {
        this.exitTime = exitTime;
    }

    public WorkingSite getEnterSite() {
        return enterSite;
    }

    public void setEnterSite(WorkingSite enterSite) {
        this.enterSite = enterSite;
    }

    public WorkingSite getExitSite() {
        return exitSite;
    }

    public void setExitSite(WorkingSite exitSite) {
        this.exitSite = exitSite;
    }

    public String getEnterLocation() {
        return enterLocation;
    }

    public void setEnterLocation(String enterLocation) {
        this.enterLocation = enterLocation;
    }

    public String getExitLocation() {
        return exitLocation;
    }

    public void setExitLocation(String exitLocation) {
        this.exitLocation = exitLocation;
    }

    public ShiftStatus getStatus() {
        return status;
    }

    public void setStatus(ShiftStatus status) {
        this.status = status;
    }

    public AbsenceReason getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(AbsenceReason absenceReason) {
        this.absenceReason = absenceReason;
    }
}