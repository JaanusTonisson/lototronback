package jks.lototronback.persistence.register;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RegisterRepository extends JpaRepository<Register, Integer> {


    boolean existsByUserIdAndLunchEventId(Integer userId, Integer lunchEventId);


    @Query("""
            select r from Register r
            where r.lunchEvent.user.id = :userId and r.lunchEvent.date >= :date and r.status = :status
            order by r.lunchEvent.date, r.lunchEvent.time""")
    List<Register> findRegistersFromDate(Integer userId, LocalDate date, String status);

    @Query("select count(r) from Register r where r.lunchEvent.id = :lunchEventId and r.status = :status")
    long countRegisteredParticipants(Integer lunchEventId, String status);


}