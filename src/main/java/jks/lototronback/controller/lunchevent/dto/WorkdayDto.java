package jks.lototronback.controller.lunchevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkdayDto {
    private LocalDate date;
    private Boolean hasAvailableLunches;
    private Integer availableLunchCount;
}
