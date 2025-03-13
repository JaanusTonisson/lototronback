package jks.lototronback.controller.lunchevent.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkdayCalendarDto {
    private Integer year;
    private Integer month;
    private List<WorkdayDto> workdays;
}
