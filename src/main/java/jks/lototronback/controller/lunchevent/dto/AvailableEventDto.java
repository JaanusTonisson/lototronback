package jks.lototronback.controller.lunchevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class AvailableEventDto {
    private Integer eventId;
    private Integer userId;
    private Integer restaurantId;
    private Integer paxTotal;
    private Integer paxAvailable;
    private String date;
    private String time;
    private String restaurantAddress;
}
