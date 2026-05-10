package server_2026_b.server.responses;

public class WorkHoursResponse extends BasicResponse{
    private Double totalHours;
    private Integer month;

    public WorkHoursResponse(boolean success, Integer errorCode, Double totalHours, Integer month) {
        super(success, errorCode);
        this.totalHours = totalHours;
        this.month = month;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

}
