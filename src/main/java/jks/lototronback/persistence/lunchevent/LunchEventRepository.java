package jks.lototronback.persistence.lunchevent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LunchEventRepository extends JpaRepository<LunchEvent, Integer> {
    @Query("select l from LunchEvent l where l.status = ?1 and l.paxAvailable > ?2")
    List<LunchEvent> findAllAvailableLunches(String status, Integer paxAvailable);


}