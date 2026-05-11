package server_2026_b.server.entities;

import java.util.Date;

public class WorkDay {
    private Long id;
    private Long userId; // id של עובד
    private Date enterTime;
    private Date exitTime;
    private WorkingSite enterSite;
    private WorkingSite exitSite;
    public WorkDay() {
    }

    public WorkDay(Long id, Long userId, Date enterTime, Date exitTime, WorkingSite enterSite, WorkingSite exitSite) {
        this.id = id;
        this.userId = userId;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.enterSite = enterSite;
        this.exitSite = exitSite;
    }

    public WorkDay(Long userId, Date enterTime, Date exitTime, WorkingSite enterSite, WorkingSite exitSite) {
        this.userId = userId;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.enterSite = enterSite;
        this.exitSite = exitSite;
    }

    public WorkDay(Long userId, Date enterTime, WorkingSite enterSite) {
        this.userId = userId;
        this.enterTime = enterTime;
        this.enterSite = enterSite;
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

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
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
}
