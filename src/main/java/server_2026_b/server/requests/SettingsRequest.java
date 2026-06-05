package server_2026_b.server.requests;
import java.time.LocalDateTime;

public class SettingsRequest {
    private LocalDateTime submissionExpiration;

    public SettingsRequest(){
    }

    public LocalDateTime getSubmissionExpiration() {
        return submissionExpiration;
    }

    public void setSubmissionExpiration(LocalDateTime submissionExpiration) {
        this.submissionExpiration = submissionExpiration;
    }
}
