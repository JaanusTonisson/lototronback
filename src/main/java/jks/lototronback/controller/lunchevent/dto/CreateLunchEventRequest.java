package jks.lototronback.controller.lunchevent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Lõuna kuupäev, peab olema tulevikus",
            example = "2025-03-20",
            format = "date",
            required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "Lõuna aeg",
            example = "12:30:00",
            format = "time",
            required = true)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
}