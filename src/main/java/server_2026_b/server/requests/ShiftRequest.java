package server_2026_b.server.requests;

public class ShiftRequest {

    private Long id;
    private Integer weekDay;
    private String startTime;
    private String endTime;
    private Integer employeeAmount;

    public ShiftRequest() {
    }

    public Long getId() {
        return id;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Integer getEmployeeAmount() {
        return employeeAmount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setEmployeeAmount(Integer employeeAmount) {
        this.employeeAmount = employeeAmount;
    }
}