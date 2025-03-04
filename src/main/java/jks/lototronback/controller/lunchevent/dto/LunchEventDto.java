package jks.lototronback.controller.lunchevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LunchEventDto {
    private Integer userId;
    private Integer restaurantId;
    private Integer paxTotal;
    private Integer paxAvailable;
    private LocalDate date;
    private LocalTime time;
        //TODO: kas siin on kõi

}
