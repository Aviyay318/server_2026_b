package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import server_2026_b.server.entities.User;
import server_2026_b.server.service.Persist;
import server_2026_b.server.utils.UserType;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class AdminRepository {

    private final Persist persist;

    public AdminRepository(Persist persist) {
        this.persist = persist;
    }

    public List<User> findAllGeneralUsers() {
        return persist.getQuerySession()
                .createQuery("FROM User WHERE userType IN (:employee, :employer)", User.class)
                .setParameter("employee", UserType.EMPLOYEE)
                .setParameter("employer", UserType.EMPLOYER)
                .list();
    }

    public List<User> findAllEmployers() {
        return persist.getQuerySession()
                .createQuery("FROM User WHERE userType = :type", User.class)
                .setParameter("type", UserType.EMPLOYER)
                .list();
    }

    public List<User> findEmployeesByEmployerId(Long employerId) {
        return persist.getQuerySession()
                .createQuery(
                        "SELECT er.employee FROM EmploymentRelation er " +
                                "WHERE er.employer.id = :employerId",
                        User.class)
                .setParameter("employerId", employerId)
                .list();
    }

    public List<User> findAllActiveUsers() {
        return persist.getQuerySession()
                .createQuery(
                        "SELECT u FROM User u, RefreshToken rt " +
                                "WHERE u.id = rt.userId " +
                                "AND rt.expiresAt > :now " +
                                "AND u.userType != :adminType",
                        User.class
                )
                .setParameter("now", LocalDateTime.now())
                .setParameter("adminType", UserType.ADMIN)
                .list();
    }

    public boolean existsById(String id) { // עם סטרינג ת״ז במקום יוזרניים
        Long count = persist.getQuerySession()
                .createQuery(
                        "SELECT COUNT(u) FROM User u " +
                                "WHERE u.id = :id AND u.userType = :userType",
                        Long.class
                )
                .setParameter("id", id)
                .setParameter("userType", UserType.EMPLOYER)
                .uniqueResult();
        return count != null && count > 0;
    }

    public void saveEmployer(User employer) {persist.save(employer);}

    public User findEmployerById(long employerId) {
        return persist.loadObject(User.class, employerId);
    }

    public void deleteAllRelationsForEmployer(long employerId) {
        persist.getQuerySession()
                .createQuery("DELETE FROM EmploymentRelation er WHERE er.employer.id = :uid")
                .setParameter("uid", employerId)
                .executeUpdate();
    }

    public void deleteEmployer(User employer) {persist.remove(employer);}

    public boolean existsByPersonalId(String personalId) {
        Long count = persist.getQuerySession()
                .createQuery("SELECT count(u) FROM User u WHERE u.personalId = :personalId", Long.class)
                .setParameter("personalId", personalId)
                .uniqueResult();

        return count != null && count > 0;
    }
}
