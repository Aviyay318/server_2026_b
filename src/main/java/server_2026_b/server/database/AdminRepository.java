package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import server_2026_b.server.entities.User;
import server_2026_b.server.service.Persist;
import server_2026_b.server.utils.UserType;

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
}
