package jks.lototronback.persistence.lunchevent;

import jks.lototronback.controller.lunchevent.dto.AvailableEventDto;
import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.status.Status;
import jks.lototronback.util.DateTimeConverter;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, imports = {Status.class})
public interface LunchEventMapper {

    @Mapping(source = "paxTotal", target = "paxTotal")
    @Mapping(source = "paxAvailable", target = "paxAvailable")
    @Mapping(source = "date", target = "date", qualifiedByName = "toLocalDate")
    @Mapping(source = "time", target = "time", qualifiedByName = "toLocalTime")
    @Mapping(expression = "java(Status.ACTIVE.getCode())", target = "status")
    @Mapping(constant = "true", target = "isAvailable")
    LunchEvent toLunchEvent(LunchEventDto lunchEventDto);

    @Named("toLocalTime")
    static LocalTime toLocalTime(String timeAsString) {
        return DateTimeConverter.stringToLocalTimeArray(timeAsString);
    }

    @Named("toLocalDate")
    static LocalDate toLocalDate(String dateAsString) {
        return DateTimeConverter.stringToLocalDateArray(dateAsString);
    }


    @Mapping(source="id", target = "eventId")
    @Mapping(source="user.id", target = "userId")
    @Mapping(source="restaurant.id", target = "restaurantId")
    @Mapping(source="paxTotal", target = "paxTotal")
    @Mapping(source="paxAvailable", target = "paxAvailable")
    @Mapping(source="date", target = "date")
    @Mapping(source="time", target = "time", qualifiedByName = "toHoursMinutes")
    @Mapping(source="restaurant.address", target = "restaurantAddress")
    AvailableEventDto toAvailableLunchEventDto(LunchEvent lunchEvent);

    List<AvailableEventDto> toAvailableLunchEventDtos(List<LunchEvent> lunchEvents);

    @Named("toHoursMinutes")
    static String toHoursMinutes(LocalTime time) {
        return time.toString(); //võtab automaatselt sekundid maha, aga töötab vaid siis, kui sekundeid on :00 (Frondist saabki vaid HH:MM)
    }

    List<AvailableEventDto> toAvailableLunchEventsByDate(List<LunchEvent> lunchEvents);

    List<AvailableEventDto> toAvailableLunchesByMonth(List<LunchEvent> lunchEvents);

    List<AvailableEventDto> toUserEventRegistrations(List<LunchEvent> lunchEvents);
}
