package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import server_2026_b.server.entities.EmployerSettings;
import server_2026_b.server.service.Persist;

import javax.transaction.Transactional;

@Repository
@Transactional
public class EmployerSettingsRepository {
    private final Persist persist;

    public EmployerSettingsRepository(Persist persist) {
        this.persist = persist;
    }
     //שליפת הגדרות לפי מזהה מעסיק!
    public EmployerSettings findByEmployerId(Long employerId) {
        if (employerId == null) {
            return null;
        }
        return persist.getQuerySession()
                .createQuery("FROM EmployerSettings WHERE employerId = :employerId", EmployerSettings.class)
                .setParameter("employerId", employerId)
                .uniqueResult();
    }

    public void save(EmployerSettings settings) {
        persist.save(settings);
    }

}
