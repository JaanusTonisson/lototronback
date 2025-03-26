package jks.lototronback.service.restaurant;

import jks.lototronback.controller.restaurant.dto.RestaurantDto;
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

    public List<RestaurantDto> getAllRestaurants() {
        return restaurantMapper.toRestaurantDtos(
                restaurantRepository.findAllByOrderByNameAsc()
        );
    }

    public RestaurantDto getRestaurantById(Integer restaurantId) {
        return restaurantMapper.toRestaurantDto(
                restaurantRepository.findById(restaurantId)
                        .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("restaurantId", restaurantId))
        );
    }

    public Restaurant getValidatedRestaurant(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("restaurantId", restaurantId));
    }
}