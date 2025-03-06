package jks.lototronback.persistence.lunchevent;

import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.status.Status;
import jks.lototronback.util.DateTimeConverter;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalTime;

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


//    private Integer userId;
//    private Integer restaurantId;
//    private Integer paxTotal;
//    private Integer paxAvailable;
//    private LocalDate date;
//    private LocalTime time;
//    LunchEventDto toDto(LunchEvent lunchEvent);
}
