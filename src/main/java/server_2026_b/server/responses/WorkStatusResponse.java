package server_2026_b.server.responses;

import java.util.Date;

public class WorkStatusResponse extends BasicResponse{
    private boolean isWorking;
    private Date startTime;

    public WorkStatusResponse(boolean success, Integer errorCode, boolean isWorking, Date startTime) {
        super(success, errorCode);
        this.isWorking = isWorking;
        this.startTime = startTime;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
