package server_2026_b.server.database;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.WorkShift;
import server_2026_b.server.service.Persist;

import java.util.List;
@Repository
@Transactional
public class WorkShiftRepository {

    private final Persist persist;

    public WorkShiftRepository(Persist persist) {
        this.persist = persist;
    }

    public void save(WorkShift workShift) {
        persist.save(workShift);
    }

    public List<WorkShift> getAll() {
        return persist.getQuerySession()
                .createQuery("FROM WorkShift", WorkShift.class)
                .list();
    }

    public List<WorkShift> getByEmployeeId(Long employeeId) {
        return persist.getQuerySession()
                .createQuery("FROM WorkShift WHERE employeeId = :employeeId", WorkShift.class)
                .setParameter("employeeId", employeeId)
                .list();
    }
}