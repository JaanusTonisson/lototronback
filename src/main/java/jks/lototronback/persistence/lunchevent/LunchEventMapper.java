package jks.lototronback.persistence.lunchevent;

import jks.lototronback.controller.lunchevent.dto.AvailableEventDto;
import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.status.Status;
import org.mapstruct.*;

import java.time.LocalTime;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LunchEventMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "restaurant.name", target = "restaurantName")
    @Mapping(source = "restaurant.address", target = "restaurantAddress")
    @Mapping(source = "paxTotal", target = "paxTotal")
    @Mapping(source = "paxAvailable", target = "paxAvailable")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "time", target = "time")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isAvailable", target = "isAvailable")
    @Mapping(target = "isJoined", ignore = true)
    @Mapping(target = "isCreator", ignore = true)
    LunchEventDto toLunchEventDto(LunchEvent lunchEvent);

    List<LunchEventDto> toLunchEventDtos(List<LunchEvent> lunchEvents);

//    //Rainiga 13.03:
//
//    @Mapping(source = "id", target = "eventId")
//    @Mapping(source = "user.id", target = "userId")
//    @Mapping(source = "restaurant.id", target = "restaurantId")
//    @Mapping(source = "paxTotal", target = "paxTotal")
//    @Mapping(source = "paxAvailable", target = "paxAvailable")
//    @Mapping(source = "date", target = "date")
//    @Mapping(source = "time", target = "time", qualifiedByName = "toHoursMinutes")
//    @Mapping(source = "restaurant.address", target = "restaurantAddress")
//    AvailableEventDto toAvailableLunchEventDto(LunchEvent lunchEvent);
//
//    List<AvailableEventDto> toAvailableLunchEventDtos(List<LunchEvent> lunchEvents);


}

