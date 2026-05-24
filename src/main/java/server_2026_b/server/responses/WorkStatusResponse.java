package server_2026_b.server.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class WorkStatusResponse extends BasicResponse {

    @JsonProperty("isWorking")
    private boolean isWorking;

    private Timestamp startTime;

    public WorkStatusResponse(boolean success, Integer errorCode, boolean isWorking, Timestamp startTime) {
        super(success, errorCode);
        this.isWorking = isWorking;
        this.startTime = startTime;
    }

    public boolean getIsWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
}