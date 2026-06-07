package server_2026_b.server.requests;

import java.time.LocalDateTime;
import java.util.List;

public class EmployeeConstraintRequest {

    public static class Constraint {
        private long shiftId;
        private boolean available;
        private String comment;

        public Constraint() {
        }

        public Constraint(long shiftId, boolean available, String comment) {
            this.shiftId = shiftId;
            this.available = available;
            this.comment = comment;
        }

        public long getShiftId() {
            return shiftId;
        }

        public void setShiftId(long shiftId) {
            this.shiftId = shiftId;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    private String employeePersonalId;
    private List<Constraint> constrains;
    private LocalDateTime date;

    public EmployeeConstraintRequest() {}

    public EmployeeConstraintRequest(String employeePersonalId, List<Constraint> constrains, LocalDateTime date) {
        this.employeePersonalId = employeePersonalId;
        this.constrains = constrains;
        this.date = date;
    }

    public String getEmployeePersonalId() {
        return employeePersonalId;
    }

    public void setEmployeePersonalId(String employeePersonalId) {
        this.employeePersonalId = employeePersonalId;
    }

    public List<Constraint> getConstrains() {
        return constrains;
    }

    public void setConstrains(List<Constraint> constrains) {
        this.constrains = constrains;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
