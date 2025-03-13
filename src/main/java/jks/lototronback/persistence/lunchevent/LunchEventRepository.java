package jks.lototronback.persistence.lunchevent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LunchEventRepository extends JpaRepository<LunchEvent, Integer> {

    @Query("SELECT l FROM LunchEvent l WHERE l.date = :date AND l.paxAvailable > 0 AND l.status != 'C' AND l.isAvailable = true ORDER BY l.time")
    List<LunchEvent> findAvailableLunchesByDate(LocalDate date);

    @Query("SELECT l FROM LunchEvent l WHERE l.user.id = :userId AND ((l.date > :today) OR (l.date = :today AND l.time >= :now)) AND l.status != 'C' ORDER BY l.date, l.time")
    List<LunchEvent> findUpcomingLunchesByUserId(Integer userId, LocalDate today, LocalTime now);

    @Query("SELECT l FROM LunchEvent l WHERE l.user.id = :userId AND ((l.date < :today) OR (l.date = :today AND l.time < :now)) ORDER BY l.date DESC, l.time DESC")
    List<LunchEvent> findPastLunchesByUserId(Integer userId, LocalDate today, LocalTime now);

    @Query("SELECT COUNT(l) FROM LunchEvent l WHERE l.date = :date AND l.paxAvailable > 0 AND l.status != 'C' AND l.isAvailable = true")
    Integer countAvailableLunchesByDate(LocalDate date);

    List<LunchEvent> findByDateAndStatusNotOrderByTime(LocalDate date, String status);
}