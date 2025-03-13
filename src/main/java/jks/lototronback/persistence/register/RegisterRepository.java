package jks.lototronback.persistence.register;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface RegisterRepository extends JpaRepository<Register, Integer> {

  List<Register> findByLunchEventId(Integer lunchEventId);

  boolean existsByUserIdAndLunchEventId(Integer userId, Integer lunchEventId);

  Optional<Register> findByUserIdAndLunchEventId(Integer userId, Integer lunchEventId);

  @Query("SELECT r FROM Register r JOIN r.lunchEvent l WHERE r.user.id = :userId " +
          "AND ((l.date > :today) OR (l.date = :today AND l.time >= :now)) " +
          "AND l.status != 'C' ORDER BY l.date, l.time")
  List<Register> findUpcomingJoinedLunchesByUserId(Integer userId, LocalDate today, LocalTime now);

  @Query("SELECT r FROM Register r JOIN r.lunchEvent l WHERE r.user.id = :userId " +
          "AND ((l.date < :today) OR (l.date = :today AND l.time < :now)) " +
          "ORDER BY l.date DESC, l.time DESC")
  List<Register> findPastJoinedLunchesByUserId(Integer userId, LocalDate today, LocalTime now);
}