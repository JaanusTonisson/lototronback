package jks.lototronback.persistence.lunchevent;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jks.lototronback.persistence.restaurant.Restaurant;
import jks.lototronback.persistence.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "lunch_event", schema = "lototron")
public class LunchEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lunch_event_id_gen")
    @SequenceGenerator(name = "lunch_event_id_gen", sequenceName = "lunch_event_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @NotNull
    @Column(name = "pax_total", nullable = false)
    private Integer paxTotal;

    @NotNull
    @Column(name = "pax_availalble", nullable = false)
    private Integer paxAvailalble;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "\"time\"", nullable = false)
    private LocalTime time;

    @Size(max = 1)
    @NotNull
    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @NotNull
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = false;

}