package jks.lototronback.persistence.restaurantdiningservice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jks.lototronback.persistence.diningservice.DiningService;
import jks.lototronback.persistence.restaurant.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurant_dining_service", schema = "lototron")
public class RestaurantDiningService {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_service_id_gen")
    @SequenceGenerator(name = "restaurant_service_id_gen", sequenceName = "restaurant_id_seq", schema = "lototron", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dining_service_id", nullable = false)
    private DiningService diningService;

}