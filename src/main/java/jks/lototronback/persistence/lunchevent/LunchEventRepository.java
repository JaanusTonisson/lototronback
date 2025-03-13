package jks.lototronback.persistence.lunchevent;

import jks.lototronback.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LunchEventRepository extends JpaRepository<LunchEvent, Integer> {

    @Query("SELECT l FROM LunchEvent l WHERE l.date = :date AND l.paxAvailable > 0 AND l.status != :cancelledStatus AND l.isAvailable = true ORDER BY l.time")
    List<LunchEvent> findAvailableLunchesByDate(LocalDate date, String cancelledStatus);

    default List<LunchEvent> findAvailableLunchesByDate(LocalDate date) {
        return findAvailableLunchesByDate(date, Status.CANCELLED.getCode());
    }

    @Query("SELECT l FROM LunchEvent l WHERE l.user.id = :userId AND ((l.date > :today) OR (l.date = :today AND l.time >= :now)) AND l.status != :cancelledStatus ORDER BY l.date, l.time")
    List<LunchEvent> findUpcomingLunchesByUserId(Integer userId, LocalDate today, LocalTime now, String cancelledStatus);

    default List<LunchEvent> findUpcomingLunchesByUserId(Integer userId, LocalDate today, LocalTime now) {
        return findUpcomingLunchesByUserId(userId, today, now, Status.CANCELLED.getCode());
    }

    @Query("SELECT l FROM LunchEvent l WHERE l.user.id = :userId AND ((l.date < :today) OR (l.date = :today AND l.time < :now)) ORDER BY l.date DESC, l.time DESC")
    List<LunchEvent> findPastLunchesByUserId(Integer userId, LocalDate today, LocalTime now);

    @Query("SELECT COUNT(l) FROM LunchEvent l WHERE l.date = :date AND l.paxAvailable > 0 AND l.status != :cancelledStatus AND l.isAvailable = true")
    Integer countAvailableLunchesByDate(LocalDate date, String cancelledStatus);

    default Integer countAvailableLunchesByDate(LocalDate date) {
        return countAvailableLunchesByDate(date, Status.CANCELLED.getCode());
    }

    List<LunchEvent> findByDateAndStatusNotOrderByTime(LocalDate date, String status);

    default List<LunchEvent> findActiveByDateOrderByTime(LocalDate date) {
        return findByDateAndStatusNotOrderByTime(date, Status.CANCELLED.getCode());
    }

    @Query("SELECT l FROM LunchEvent l WHERE l.user.id = :userId AND l.date = :date AND l.status != :cancelledStatus")
    List<LunchEvent> findByUserIdAndDate(Integer userId, LocalDate date, String cancelledStatus);

    default List<LunchEvent> findByUserIdAndDate(Integer userId, LocalDate date) {
        return findByUserIdAndDate(userId, date, Status.CANCELLED.getCode());
    }
}