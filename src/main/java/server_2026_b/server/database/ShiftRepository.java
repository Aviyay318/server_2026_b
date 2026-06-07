package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.Shift;
import server_2026_b.server.service.Persist;

import java.util.List;

@Repository
@Transactional
public class ShiftRepository {
    private final Persist persist;
    public ShiftRepository(Persist persist) {
        this.persist = persist;
    }

    public void save(Shift shift) {
        persist.save(shift);
    }

    public void saveAll(List<Shift> shifts) {
        if (shifts == null) {
            return;
        }
        for (Shift shift : shifts) {
            save(shift);
        }
    }

    public Shift findById(Long id) {
        if (id == null) {
            return null;
        }
        return persist.getQuerySession().get(Shift.class, id);
    }

    public List<Shift> findAllByEmployerId(Long employerId) {
        return persist.getQuerySession()
                .createQuery("FROM Shift WHERE employerId = :employerId ORDER BY weekDay, startTime", Shift.class)
                .setParameter("employerId", employerId).list();
    }

    public List<Shift> findActiveByEmployerId(Long employerId) {
        return persist.getQuerySession()
                .createQuery("FROM Shift WHERE employerId = :employerId AND active = true ORDER BY weekDay, startTime", Shift.class)
                .setParameter("employerId", employerId).list();
    }

    public void delete(Shift shift) {
        if (shift == null) {
            return;
        }
        persist.getQuerySession().delete(shift);
    }
}