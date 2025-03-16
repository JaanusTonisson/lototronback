package jks.lototronback.persistence.userimage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Integer> {

  @Query("select u from UserImage u where u.user.id = :userId")
  Optional<UserImage> findUserImageByUserId(Integer userId);
}