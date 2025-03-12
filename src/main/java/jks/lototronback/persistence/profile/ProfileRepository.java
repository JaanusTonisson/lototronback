package jks.lototronback.persistence.profile;

import jks.lototronback.persistence.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
   Optional<Profile> findByUser(User user);
}
