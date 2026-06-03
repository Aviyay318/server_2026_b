package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import server_2026_b.server.entities.EmployeeConstraint;
import server_2026_b.server.service.Persist;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public class EmployeeConstraintsRepository {

    private final Persist persist;

    public EmployeeConstraintsRepository(Persist persist) {
        this.persist = persist;
    }

    public boolean constraintExists(String employeeId, long shiftId, Timestamp date) {
        Long count = persist.getQuerySession()
                .createQuery(
                        "SELECT COUNT(constraint) FROM EmployeeConstraint constraint " +
                                "WHERE constraint.employeeId = :employeeId " +
                                "AND constraint.shiftId = :shiftId " +
                                "AND constraint.date = :date",
                        Long.class
                )
                .setParameter("employeeId", employeeId)
                .setParameter("shiftId", shiftId)
                .setParameter("date", date)
                .uniqueResult();

        return count != null && count > 0;
    }

    public void createConstraint(EmployeeConstraint constraint) {
        persist.save(constraint);
    }

    public List<EmployeeConstraint> getAllConstraints() {
        return persist.getQuerySession()
                .createQuery("FROM EmployeeConstraint", EmployeeConstraint.class)
                .list();
    }
}
