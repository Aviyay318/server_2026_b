package server_2026_b.server.entities;

import java.time.LocalDateTime;

public class EmployerSettings {
    private Long id;
    private Long employerId;
    private LocalDateTime submissionExpiration;

    public EmployerSettings( Long employerId, LocalDateTime submissionExpiration) {
        this.employerId = employerId;
        this.submissionExpiration = submissionExpiration;
    }

    public EmployerSettings() {
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

    public LocalDateTime getSubmissionExpiration() {
        return submissionExpiration;
    }

    public void setSubmissionExpiration(LocalDateTime submissionExpiration) {
        this.submissionExpiration = submissionExpiration;
    }
}
