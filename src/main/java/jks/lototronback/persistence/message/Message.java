package jks.lototronback.persistence.message;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jks.lototronback.persistence.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "message", schema = "lototron")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_id_gen")
    @SequenceGenerator(name = "message_id_gen", sequenceName = "message_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private User receiverUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User senderUser;

    @Size(max = 255)
    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @Size(max = 1000)
    @NotNull
    @Column(name = "body", nullable = false, length = 1000)
    private String body;

    @Size(max = 1)
    @NotNull
    @Column(name = "sender_type", nullable = false, length = 1)
    private String senderType;

    @Size(max = 1)
    @NotNull
    @Column(name = "state", nullable = false, length = 1)
    private String state;

}