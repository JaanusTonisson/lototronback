package jks.lototronback.controller.lunchevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinLunchDto {
    private Integer userId;
    private Integer eventId;
}
