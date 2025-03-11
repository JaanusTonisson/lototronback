package jks.lototronback.persistence.register;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Register, Integer> {
  boolean existsByUserIdAndLunchEventId(Integer userId, Integer lunchEventId);
  }