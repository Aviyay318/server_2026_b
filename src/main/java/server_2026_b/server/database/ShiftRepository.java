package server_2026_b.server.database;
import server_2026_b.server.service.Persist;
import org.springframework.stereotype.Repository;
import server_2026_b.server.entities.Shift;
import java.util.Date;
import java.util.List;

@Repository
public class ShiftRepository {

    private final Persist persist;

    public ShiftRepository(Persist persist) {
        this.persist = persist;
    }

    public void save(Shift shift) {
        persist.save(shift);
    }
    public List<Shift> findShiftsByUserId(long userId,Date fromDate, Date  toDate) {
        return persist.getQuerySession()
                .createQuery(
                    "FROM Shift WHERE userId = :userId AND startTime >= :fromDate AND endTime <= :toDate ORDER BY startTime DESC",
                    Shift.class)
                    .setParameter("userId", userId)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .list();
    }
    
}
