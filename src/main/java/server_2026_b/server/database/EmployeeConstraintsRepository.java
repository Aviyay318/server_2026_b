package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import server_2026_b.server.entities.EmployeeConstraint;
import server_2026_b.server.service.Persist;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class EmployeeConstraintsRepository {

    private final Persist persist;

    public EmployeeConstraintsRepository(Persist persist) {
        this.persist = persist;
    }

    public boolean constraintExists(String employeePersonalId, long shiftId, Timestamp date) {
        Long count = persist.getQuerySession()
                .createQuery(
                        "SELECT COUNT(c) FROM EmployeeConstraint c " +
                                "WHERE c.employee.personalId = :employeePersonalId " +
                                "AND c.shift.id = :shiftId " +
                                "AND c.date = :date",
                        Long.class
                )
                .setParameter("employeePersonalId", employeePersonalId)
                .setParameter("shiftId", shiftId)
                .setParameter("date", date) // Hibernate will happily match Timestamp with type="timestamp"
                .uniqueResult();

        return count != null && count > 0;
    }

    public void createConstraint(EmployeeConstraint constraint) {
        persist.save(constraint);
    }

    public void saveList(List<EmployeeConstraint> employeeConstraintList){
        persist.saveAll(employeeConstraintList);
    }

    public List<EmployeeConstraint> getAllConstraints() {
        return persist.getQuerySession()
                .createQuery("FROM EmployeeConstraint", EmployeeConstraint.class)
                .list();
    }
}