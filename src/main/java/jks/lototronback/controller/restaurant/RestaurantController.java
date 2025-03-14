package jks.lototronback.controller.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import jks.lototronback.controller.restaurant.dto.RestaurantDto;
import jks.lototronback.service.restaurant.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/restaurants")
    @Operation(summary = "Too kõik restoranid")
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Too restaurantId'ga üks restoran")
    public RestaurantDto getRestaurantById(@PathVariable Integer restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }
}