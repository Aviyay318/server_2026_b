package server_2026_b.server.entities;

import java.util.Date;

public class WorkDay {
    private Long id;
    private Long userId; // id של עובד
    private Date enterTime;
    private Date exitTime;
    private String location;

    public WorkDay() {
    }

    public WorkDay(Long id, Long userId, Date enterTime, Date exitTime, String location) {
        this.id = id;
        this.userId = userId;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
