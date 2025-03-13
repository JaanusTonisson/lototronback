package jks.lototronback.persistence.lunchevent;

import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.status.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, imports = {Status.class})
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
    LunchEventDto toLunchEventDto(LunchEvent lunchEvent);

    List<LunchEventDto> toLunchEventDtos(List<LunchEvent> lunchEvents);
}


