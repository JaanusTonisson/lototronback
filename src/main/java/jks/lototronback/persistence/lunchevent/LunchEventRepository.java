package jks.lototronback.persistence.lunchevent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LunchEventRepository extends JpaRepository<LunchEvent, Integer> {
    @Query("select l from LunchEvent l where l.status = ?1 and l.date >= ?2 and l.paxAvailable > 0")
    List<LunchEvent> findAllAvailableTodayAndFutureLunches(String status, LocalDate nowDate);

    @Query("select l from LunchEvent l where l.user.id = ?1 and l.date >= ?2 and l.status = ?3")
    List<LunchEvent> findUserCreatedTodayAndFutureLunches(Integer userId, LocalDate nowDate, String status);

    @Query("select l from LunchEvent l where l.status = ?1 and l.date = ?2 and l.paxAvailable > 0")
    List<LunchEvent> findAllAvailableLunchesByDate(String status, LocalDate nowDate);

    @Query("select l from LunchEvent l where l.status = ?1 and YEAR(l.date) = ?2 and MONTH(l.date) = ?3 and l.paxAvailable > 0")
    List<LunchEvent> findAllAvailableLunchesByMonth(String status, int year, int month);

    @Query("select l from LunchEvent l where l.id IN (select r.lunchEvent.id from Register r where r.user.id = ?1) and l.date >= ?2")
    List<LunchEvent> findAllUserEventRegistrations(Integer userId, LocalDate nowDate);
}