package jks.lototronback.service.restaurant;

import jks.lototronback.controller.restaurant.dto.RestaurantInfo;
import jks.lototronback.persistence.restaurant.Restaurant;
import jks.lototronback.persistence.restaurant.RestaurantMapper;
import jks.lototronback.persistence.restaurant.RestaurantRepository;
import jks.lototronback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    public List<RestaurantInfo> getRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<RestaurantInfo> restaurantInfos = restaurantMapper.toRestaurantInfos(restaurants);
        return restaurantInfos;
    }

    public Restaurant getValidatedRestaurant(Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("restaurandId", restaurantId));
        return restaurant;
    }
}

