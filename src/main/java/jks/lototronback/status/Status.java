package jks.lototronback.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ACTIVE("A"),
    FULL("F"),
    DEACTIVATED("D"),
    CANCELLED("C");

    private final String code;

}
