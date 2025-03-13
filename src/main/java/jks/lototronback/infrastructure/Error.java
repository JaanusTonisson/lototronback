package jks.lototronback.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Error {
    INCORRECT_CREDENTIALS("Vale kasutajanimi või parool", 111),
    PRIMARY_KEY_NOT_FOUND("Ei leidnud primary keyd: ", 888),
    FOREIGN_KEY_NOT_FOUND("Ei leidnud foreign keyd: ", 999),
    USER_NOT_FOUND("Kasutajat ei leitud ", 404),
    INCORRECT_PASSWORD("Vale parool", 112);

    private final String message;
    private final int errorCode;
}
