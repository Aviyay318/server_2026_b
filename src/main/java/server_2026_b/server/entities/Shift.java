package server_2026_b.server.entities;

import java.util.Date;

// Shift = weekly shift requirement created by employer.
// This is not the final employee work placement.
// Actual employee placement will be saved later.
public class Shift {

    private Long id;
    private Long employerId;
    private Boolean active;
    private Integer weekDay;
    private Date startTime;
    private Date endTime;
    private Integer employeeAmount;

    public Shift() {
    }

    public Shift(Long id, Long employerId, Boolean active, Integer weekDay,
                 Date startTime, Date endTime, Integer employeeAmount) {
        this.id = id;
        this.employerId = employerId;
        this.active = active;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employeeAmount = employeeAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
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

    public Integer getEmployeeAmount() {
        return employeeAmount;
    }

    public void setEmployeeAmount(Integer employeeAmount) {
        this.employeeAmount = employeeAmount;
    }
}