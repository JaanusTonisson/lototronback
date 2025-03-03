package jks.lototronback.persistence.restaurantservice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jks.lototronback.persistence.service.Service;
import jks.lototronback.persistence.restaurant.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurant_service", schema = "lototron")
public class RestaurantService {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_service_id_gen")
    @SequenceGenerator(name = "restaurant_service_id_gen", sequenceName = "restaurant_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

}