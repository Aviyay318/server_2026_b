package server_2026_b.server.responses;

import java.util.List;

public class EmployeeConstraintsResponse extends BasicResponse { // תחזיר את רשימת האילוצים ללא auto increment id (אובייקט מסוג EmployeeConstraintResponse נטו בשביל זה)

    private List<EmployeeConstraintResponse> constraints;

    public EmployeeConstraintsResponse(
            boolean success,
            Integer errorCode,
            List<EmployeeConstraintResponse> constraints) {

        super(success, errorCode);
        this.constraints = constraints;
    }

    public List<EmployeeConstraintResponse> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<EmployeeConstraintResponse> constraints) {
        this.constraints = constraints;
    }
}
