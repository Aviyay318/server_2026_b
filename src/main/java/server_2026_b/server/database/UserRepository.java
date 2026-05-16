package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.User;
import server_2026_b.server.service.Persist;
import server_2026_b.server.utils.UserType;

@Repository
public class UserRepository {
    private final Persist persist;
    public UserRepository(Persist persist) {
        this.persist = persist;
    }

    @Transactional
    public User findUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        return persist.loadObject(User.class, userId);
    }

    @Transactional
    public User findUserByUsernamePasswordAndType(String username, String password, UserType userType) {
        if (username == null || password == null || userType == null) {
            return null;
        }
        return persist.getQuerySession()
                .createQuery(
                        "FROM User u " +
                                "WHERE u.username = :username " +
                                "AND u.password = :password " +
                                "AND u.userType = :userType",
                        User.class
                )
                .setParameter("username", username)
                .setParameter("password", password)
                .setParameter("userType", userType)
                .uniqueResult();
    }


}