package jks.lototronback.persistence.lunchevent;

import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LunchEventMapper {
    LunchEvent toEntity(LunchEventDto lunchEventDto);

    LunchEventDto toDto(LunchEvent lunchEvent);

}