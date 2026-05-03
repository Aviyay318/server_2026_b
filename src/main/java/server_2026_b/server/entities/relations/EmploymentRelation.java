package server_2026_b.server.entities.relations;

import server_2026_b.server.entities.Employee;
import server_2026_b.server.entities.Employer;

public class EmploymentRelation {

    private Long id;
    private Employer employer;
    private Employee employee;


    public EmploymentRelation() {
    }

    public EmploymentRelation(Employer employer, Employee employee) {
        this.employer = employer;
        this.employee = employee;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


}