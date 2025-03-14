package jks.lototronback.persistence.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.username = :username and u.password = :password and u.status = :status")
    Optional<User> findUserBy(String username, String password, String status);

    boolean existsByUsername(String username);

    @Query("select u from User u where u.id = :userId and u.password = :password")
    Optional<User> findUserBy(Integer userId, String password);

}