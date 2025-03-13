package jks.lototronback.controller.lunchevent.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLunchEventRequest {
    private Integer restaurantId;
    private Integer paxTotal;
    private LocalDate date;
    private LocalTime time;
}