package server_2026_b.server.entities.relations;

import server_2026_b.server.entities.User;

public class EmploymentRelation {

    private Long id;
    private User employer;
    private User employee;

    public EmploymentRelation() {
    }

    public EmploymentRelation(User employer, User employee) {
        this.employer = employer;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}