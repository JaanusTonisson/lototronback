package jks.lototronback.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ACTIVE("A"),
    DEACTIVATED("D");

    private final String code;

}
