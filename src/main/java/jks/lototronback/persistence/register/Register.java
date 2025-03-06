package jks.lototronback.persistence.register;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jks.lototronback.persistence.lunchevent.LunchEvent;
import jks.lototronback.persistence.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "register", schema = "lototron")
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "register_id_gen")
    @SequenceGenerator(name = "register_id_gen", sequenceName = "register_id_seq", schema = "lototron", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lunch_event_id", nullable = false)
    private LunchEvent lunchEvent;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

}