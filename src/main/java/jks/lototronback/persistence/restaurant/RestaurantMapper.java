package jks.lototronback.persistence.restaurant;

import jks.lototronback.controller.restaurant.dto.RestaurantDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
    RestaurantDto toRestaurantDto(Restaurant restaurant);

    List<RestaurantDto> toRestaurantDtos(List<Restaurant> restaurants);
}