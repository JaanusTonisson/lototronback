package jks.lototronback.controller.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link jks.lototronback.persistence.register.Register}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInfo implements Serializable {
    private Integer lunchEventId;
    private String date;
    private String time;
    private String restaurant;
    private Long paxRegistered;
}