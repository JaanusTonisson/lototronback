package jks.lototronback.persistence.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByReceiverUserIdOrderByIdDesc(Integer receiverId);

    List<Message> findByReceiverUserIdAndStateOrderByIdDesc(Integer receiverId, String state);

    int countByReceiverUserIdAndState(Integer receiverId, String state);
}