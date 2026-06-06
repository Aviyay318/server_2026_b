package server_2026_b.server.entities;

import java.util.Date;

public class WorkShift {
    private Long id;
    private Long employeeId;
    private Long shiftId;
    private Date date;

    public WorkShift() {}

    public WorkShift(Long employeeId, Long shiftId, Date date) {
        this.employeeId = employeeId;
        this.shiftId = shiftId;
        this.date = date;
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

    public Long getShiftId() { 
        return shiftId; 
    }
    public void setShiftId(Long shiftId) { 
        this.shiftId = shiftId; 
    }

    public Date getDate() { 
        return date; 
    }
    public void setDate(Date date) {
        this.date = date;
    }
}