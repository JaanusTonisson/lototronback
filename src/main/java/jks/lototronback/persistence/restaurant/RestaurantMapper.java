package jks.lototronback.persistence.restaurant;

import jks.lototronback.controller.restaurant.dto.RestaurantInfo;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantMapper {


//    private Integer restaurantId;
//    private String restaurantName;
//    private String address;
    @Mapping(source = "id", target = "restaurantId")
    @Mapping(source = "name", target = "restaurantName")
    @Mapping(source = "address", target = "address")
    RestaurantInfo toRestaurantInfo(Restaurant restaurant);

    List<RestaurantInfo> toRestaurantInfos(List<Restaurant> restaurants);

}