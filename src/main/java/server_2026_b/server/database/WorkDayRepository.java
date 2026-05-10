package server_2026_b.server.database;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.WorkDay;
import server_2026_b.server.service.Persist;

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
                        "FROM WorkDay WHERE userId = :userId AND exitTime IS NULL ORDER BY enterTime DESC",
                        WorkDay.class
                )
                .setParameter("userId", userId)
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
            if (workDay.getEnterTime() != null && workDay.getExitTime() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();//ספרייה ששולפת חודש
                cal.setTime(workDay.getEnterTime());
                int workDayMonth = cal.get(java.util.Calendar.MONTH) + 1; //ינואר הוא 0, לכן מוסיפים 1

                if (workDayMonth == month) {
                    long diffInMillis = workDay.getExitTime().getTime() - workDay.getEnterTime().getTime();
                    double hoursInDay = (double) diffInMillis / 3600000.0;//להפוך ממילי שניות לשעות
                    totalHours += hoursInDay;
                }
            }
        }
        return totalHours;
    }
}
