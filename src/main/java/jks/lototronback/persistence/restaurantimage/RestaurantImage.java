package jks.lototronback.persistence.restaurantimage;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jks.lototronback.persistence.restaurant.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurant_image", schema = "lototron")
public class RestaurantImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_image_id_gen")
    @SequenceGenerator(name = "restaurant_image_id_gen", sequenceName = "restaurant_image_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @NotNull
    @Column(name = "data", nullable = false)
    private byte[] data;

}