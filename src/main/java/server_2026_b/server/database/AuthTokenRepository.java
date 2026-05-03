package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.AuthToken;
import server_2026_b.server.service.Persist;

@Repository
public class AuthTokenRepository {

    private final Persist persist;

    public AuthTokenRepository(Persist persist) {
        this.persist = persist;
    }

    @Transactional
    public AuthToken findByToken(String token) {
        return persist.getQuerySession()
                .createQuery("FROM AuthToken WHERE token = :token", AuthToken.class)
                .setParameter("token", token)
                .uniqueResult();
    }

    public void save(AuthToken authToken) {
        persist.save(authToken);
    }

    public void delete(AuthToken authToken) {
        persist.remove(authToken);
    }
}