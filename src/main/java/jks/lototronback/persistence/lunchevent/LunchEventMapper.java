package jks.lototronback.persistence.lunchevent;

import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LunchEventMapper {

//    private Integer userId;
//    private Integer restaurantId;
//    private Integer paxTotal;
//    private Integer paxAvailable;
//    private LocalDate date;
//    private LocalTime time;

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "restaurantId", target = "restaurant.id")
    @Mapping(source = "paxTotal", target = "paxTotal")
    @Mapping(source = "paxAvailable", target = "paxAvailable")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "time", target = "time")
    LunchEvent toLunchevent(LunchEventDto lunchEventDto);

//    private Integer userId;
//    private Integer restaurantId;
//    private Integer paxTotal;
//    private Integer paxAvailable;
//    private LocalDate date;
//    private LocalTime time;
    LunchEventDto toDto(LunchEvent lunchEvent);

}