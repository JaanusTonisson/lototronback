package jks.lototronback.controller.lunchevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDto {
    private List<CalendarDayDto> days;
}
