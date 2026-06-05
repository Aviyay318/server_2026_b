package server_2026_b.server.responses;

import java.time.LocalDateTime;

public class SettingsResponse extends BasicResponse{
    private LocalDateTime submissionExpiration;

    public SettingsResponse(boolean success, LocalDateTime submissionExpiration) {
        super(success, null);
        this.submissionExpiration = submissionExpiration;
    }

    public SettingsResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public LocalDateTime getSubmissionExpiration() {
        return submissionExpiration;
    }

    public void setSubmissionExpiration(LocalDateTime submissionExpiration) {
        this.submissionExpiration = submissionExpiration;
    }
}
