package jks.lototronback.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ACTIVE("A"),
    DELETED("D");

    private final String code;

}
