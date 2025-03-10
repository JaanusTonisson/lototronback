package jks.lototronback.persistence.lunchevent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface LunchEventRepository extends JpaRepository<LunchEvent, Integer> {
    @Query("select l from LunchEvent l where l.status = ?1 and l.date >= ?2 and l.paxAvailable > ?3")
    List<LunchEvent> findAllAvailableTodayAndFutureLunches(String status, LocalDate nowDate, Integer paxAvailable);

    @Query("select l from LunchEvent l where l.user.id = ?1 and l.date >= ?2 and l.status = ?3")
    List<LunchEvent> findUserCreatedTodayAndFutureLunches(Integer userId, LocalDate nowDate, String status);

    @Query("select l from LunchEvent l where l.status = ?1 and l.date = ?2 and l.paxAvailable = ?3")
    List<LunchEvent> findAllAvailableLunchesByDate(String status, LocalDate nowDateString, Integer paxAvailable);


}