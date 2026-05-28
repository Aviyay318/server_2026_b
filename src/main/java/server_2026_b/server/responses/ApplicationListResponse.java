package server_2026_b.server.responses;

import server_2026_b.server.entities.Application;

import java.util.List;

public class ApplicationListResponse extends BasicResponse {
    private List<Application> applications;

    public ApplicationListResponse(boolean success, Integer errorCode, List<Application> applications) {
        super(success, errorCode);
        this.applications = applications;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
