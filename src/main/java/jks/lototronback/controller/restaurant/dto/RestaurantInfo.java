package jks.lototronback.controller.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInfo {
    private Integer restaurantId;
    private String restaurantName;
    private String address;


}
