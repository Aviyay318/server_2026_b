package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.RefreshToken;
import server_2026_b.server.entities.User;
import server_2026_b.server.service.Persist;

@Repository
public class RefreshTokenRepository {

    private final Persist persist;
    public RefreshTokenRepository(Persist persist) {
        this.persist = persist;
    }

    @Transactional
    public RefreshToken findByToken(String token) {
        return persist.getQuerySession()
                .createQuery("FROM server_2026_b.server.entities.RefreshToken WHERE token = :token",
                        RefreshToken.class)
                .setParameter("token", token)
                .uniqueResult();
    }

    @Transactional
    public RefreshToken findByUserId(Long userId) {
        return persist.getQuerySession()
                .createQuery( "FROM server_2026_b.server.entities.RefreshToken WHERE userId = :userId",
                        RefreshToken.class)
                .setParameter("userId", userId)
                .uniqueResult();
    }

    @Transactional
    public void save(RefreshToken refreshToken) {
        persist.save(refreshToken);
    }

    @Transactional
    public void delete(RefreshToken refreshToken) {
        if (refreshToken != null) {
            persist.remove(refreshToken);
        }
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        RefreshToken refreshToken = findByUserId(userId);

        if (refreshToken != null) {
            persist.remove(refreshToken);
        }
    }
}