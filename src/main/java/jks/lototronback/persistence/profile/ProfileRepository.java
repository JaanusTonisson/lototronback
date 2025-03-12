package jks.lototronback.persistence.profile;

import jakarta.validation.constraints.NotNull;
import jks.lototronback.persistence.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByUser(User user);

    List<Profile> user(@NotNull User user);

    @Query("select p from Profile p where p.user.id = :userId")
    Optional<Profile> findProfileBy(Integer userId);


}
