package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.WorkDay;
import server_2026_b.server.service.Persist;

import java.util.List;

@Repository
public class WorkDayRepository {

    private final Persist persist;


    public WorkDayRepository(Persist persist) {
        this.persist = persist;
    }

    public void save(WorkDay workDay) {
        persist.save(workDay);
    }

    @Transactional
    public WorkDay findOpenByUserId(Long userId) {
        return persist.getQuerySession()
                .createQuery(
                        "FROM WorkDay WHERE userId = :userId AND exitTime IS NULL ORDER BY enterTime DESC",
                        WorkDay.class
                )
                .setParameter("userId", userId)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Transactional
    public List<WorkDay> findAllByUserId(Long userId) {
        return persist.getQuerySession()
                .createQuery(
                        "FROM WorkDay WHERE userId = :userId ORDER BY enterTime DESC",
                        WorkDay.class
                )
                .setParameter("userId", userId)
                .list();
    }
}
