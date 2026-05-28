package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.Application;
import server_2026_b.server.service.Persist;
import server_2026_b.server.utils.ApplicationStatus;

import java.util.List;

@Repository
@Transactional
public class ApplicationRepository {

    private final Persist persist;

    public ApplicationRepository(Persist persist) {
        this.persist = persist;
    }

    public void save(Application application) {
        persist.save(application);
    }

    public Application findById(Long id) {
        if (id == null) return null;
        return persist.loadObject(Application.class, id);
    }

    public List<Application> findWaitingByEmployer(Long employerId) {
        return persist.getQuerySession()
                .createQuery(
                        "FROM Application WHERE employerId = :eid AND status = :status ORDER BY date DESC",
                        Application.class
                )
                .setParameter("eid", employerId)
                .setParameter("status", ApplicationStatus.WAITING)
                .list();
    }
}
