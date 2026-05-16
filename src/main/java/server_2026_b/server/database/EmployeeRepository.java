package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.ArchivedEmployee;
import server_2026_b.server.entities.User;
import server_2026_b.server.entities.relations.EmploymentRelation;
import server_2026_b.server.service.Persist;
import server_2026_b.server.utils.ShiftStatus;

import java.util.List;

@Repository
@Transactional
public class EmployeeRepository {

    private final Persist persist;

    public EmployeeRepository(Persist persist) {
        this.persist = persist;
    }

    public boolean existsByUsername(String username) {
        Long count = persist.getQuerySession()
                .createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .uniqueResult();
        return count != null && count > 0;
    }

    public void saveEmployee(User employee) {
        persist.save(employee);
    }

    public void saveRelation(EmploymentRelation relation) {
        persist.save(relation);
    }

    public User findEmployeeById(Long employeeId) {
        if (employeeId == null) return null;
        return persist.loadObject(User.class, employeeId);
    }

    public EmploymentRelation findRelation(Long employerId, Long employeeId) {
        return persist.getQuerySession()
                .createQuery(
                        "FROM EmploymentRelation er " +
                        "WHERE er.employer.id = :eid AND er.employee.id = :uid",
                        EmploymentRelation.class
                )
                .setParameter("eid", employerId)
                .setParameter("uid", employeeId)
                .uniqueResult();
    }

    public List<User> findActiveEmployeesByEmployer(Long employerId) {
        return persist.getQuerySession()
                .createQuery(
                        "SELECT er.employee FROM EmploymentRelation er " +
                        "WHERE er.employer.id = :eid " +
                        "AND er.employee.id IN (" +
                        "   SELECT wd.userId FROM WorkDay wd WHERE wd.status = :status" +
                        ")",
                        User.class
                )
                .setParameter("eid", employerId)
                .setParameter("status", ShiftStatus.IN_PROGRESS)
                .list();
    }

    public void deleteAllRelationsForEmployee(Long employeeId) {
        persist.getQuerySession()
                .createQuery("DELETE FROM EmploymentRelation er WHERE er.employee.id = :uid")
                .setParameter("uid", employeeId)
                .executeUpdate();
    }

    public void deleteEmployee(User employee) {
        persist.remove(employee);
    }

    public void archiveEmployee(ArchivedEmployee archived) {
        persist.save(archived);
    }
}
