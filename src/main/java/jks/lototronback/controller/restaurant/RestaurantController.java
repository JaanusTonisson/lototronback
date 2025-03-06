package jks.lototronback.controller.restaurant;

import jks.lototronback.controller.restaurant.dto.RestaurantInfo;
import jks.lototronback.persistence.restaurant.Restaurant;
import jks.lototronback.service.restaurant.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    @GetMapping("/restaurants")
    public List<RestaurantInfo> getRestaurants() {
        List<RestaurantInfo> restaurants = restaurantService.getRestaurants();
        return restaurants;

    }
    @GetMapping("/restaurant")
    public Restaurant getValidatedRestaurant(Integer restaurantId) {
        Restaurant restaurant = restaurantService.getValidatedRestaurant(restaurantId);
        return restaurant;
    }



}
