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
    INCORRECT_PASSWORD("Vale parool", 112),
    LUNCH_IN_PAST("Ei saa planeerida lõunad möödunud kuupäevale", 2001),
    LUNCH_ON_WEEKEND("Lõunat saab planeerida ainult tööpäevale", 2002),
    LUNCH_NO_SPOTS("Sellele lõunale ei ole vabat kohta", 2003),
    LUNCH_ALREADY_JOINED("Sa oled juba liitunud selle lõunaga", 2004),
    LUNCH_EVENT_CREATOR("Sa oled selle lõuna looja", 2005),
    LUNCH_EVENT_CANCELED("See lõuna on tühistatud", 2006),
    NOT_YOUR_LUNCH("Sa saad muuda ainult enda loodud lõunaid", 2007),
    MIN_PAX_REQUIRED("Sa ei saa muuta osalejate arvu väiksemaks kui on juba liitujaid", 2008),
    NO_LUNCH_FOUND("Ühtegi lõunat ei ole leitud", 2009);

    private final String message;
    private final int errorCode;
}
