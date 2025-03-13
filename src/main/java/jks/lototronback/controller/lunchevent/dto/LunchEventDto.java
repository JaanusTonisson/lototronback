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
    private Integer id;
    private Integer userId;
    private Integer restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private Integer paxTotal;
    private Integer paxAvailable;
    private LocalDate date;
    private LocalTime time;
    private String status;
    private Boolean isAvailable;
    private Boolean isJoined;
    private Boolean isCreator;
}
