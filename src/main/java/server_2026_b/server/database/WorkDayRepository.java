package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.WorkDay;
import server_2026_b.server.entities.WorkingSite;
import server_2026_b.server.service.Persist;
import server_2026_b.server.utils.ShiftStatus;

import java.util.List;

@Repository
@Transactional
public class WorkDayRepository {

    private final Persist persist;

    public WorkDayRepository(Persist persist) {
        this.persist = persist;
    }

    public void save(WorkDay workDay) {
        persist.save(workDay);
    }

    public WorkDay findOpenByUserId(Long userId) {
        return persist.getQuerySession()
                .createQuery(
                        "FROM WorkDay WHERE userId = :userId AND status = :status ORDER BY enterTime DESC",
                        WorkDay.class
                )
                .setParameter("userId", userId)
                .setParameter("status", ShiftStatus.IN_PROGRESS)
                .setMaxResults(1)
                .uniqueResult();
    }

    public List<WorkDay> findAllByUserId(Long userId) {
        return persist.getQuerySession()
                .createQuery(
                        "FROM WorkDay WHERE userId = :userId ORDER BY enterTime DESC",
                        WorkDay.class
                )
                .setParameter("userId", userId)
                .list();
    }

    public Double getTotalHoursByMonth(Long userId, int month) {
        List<WorkDay> allWorkDays = findAllByUserId(userId);
        double totalHours = 0;

        for (WorkDay workDay : allWorkDays) {
            if (workDay.getStatus() == ShiftStatus.ABSENCE) {
                continue;
            }

            if (workDay.getEnterTime() != null && workDay.getExitTime() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(workDay.getEnterTime());

                int workDayMonth = cal.get(java.util.Calendar.MONTH) + 1;

                if (workDayMonth == month) {
                    long diffInMillis = workDay.getExitTime().getTime() - workDay.getEnterTime().getTime();
                    double hoursInDay = (double) diffInMillis / 3600000.0;
                    totalHours += hoursInDay;
                }
            }
        }

        return totalHours;
    }

    public WorkingSite findSiteById(Long siteId) {
        return persist.loadObject(WorkingSite.class, siteId);
    }

    public List<WorkingSite> findAllSites() {
        return persist.loadList(WorkingSite.class);
    }
}