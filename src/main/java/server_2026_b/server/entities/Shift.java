package server_2026_b.server.entities;
import java.util.Date;

public class Shift {
    private Long id;
    private Long userId;
    private Date startTime;
    private Date endTime;
    private String location;
    private WorkingSite workSite;
    private String position;

    public Shift() {
    }

    
    public Shift(Long id, Long userId, Date startTime, Date endTime, String location, WorkingSite workSite, String position) {
        this.id = id;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.workSite = workSite;
        this.position = position;
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
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public WorkingSite getWorkSite() {
        return workSite;
    }
    public void setWorkSite(WorkingSite workSite) {
        this.workSite = workSite;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    
}
