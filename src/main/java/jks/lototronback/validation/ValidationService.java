package jks.lototronback.validation;

import jks.lototronback.infrastructure.exception.DataNotFoundException;
import jks.lototronback.infrastructure.exception.ForbiddenException;
import jks.lototronback.persistence.lunchevent.LunchEvent;
import jks.lototronback.status.Status;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static jks.lototronback.infrastructure.Error.*;

public class ValidationService {

    // Existing methods
    public static DataNotFoundException throwPrimaryKeyNotFoundException(String primaryKeyName, Integer value) {
        return new DataNotFoundException(PRIMARY_KEY_NOT_FOUND.getMessage() + primaryKeyName + " = " + value, PRIMARY_KEY_NOT_FOUND.getErrorCode());
    }

    public static DataNotFoundException throwForeignKeyNotFoundException(String fieldName, Integer value) {
        return new DataNotFoundException(FOREIGN_KEY_NOT_FOUND.getMessage() + fieldName + " = " + value, FOREIGN_KEY_NOT_FOUND.getErrorCode());
    }

    // New lunch-specific validation methods
    public static void validateLunchDateAndTime(LocalDate date, LocalTime time) {
        LocalDate today = LocalDate.now();

        // Check if date is in the past
        if (date.isBefore(today)) {
            throw new ForbiddenException("Ei saa planeerida lõunat möödunud päevale", 2001);
        }

        // Check if date is today but time is in the past
        if (date.equals(today) && time.isBefore(LocalTime.now())) {
            throw new ForbiddenException("Ei saa planeerida lõunad möödunud ajale", 2002);
        }

        // Check if date is a workday (Monday to Friday)
        if (!isWorkday(date)) {
            throw new ForbiddenException("Lõunat saab planeerida ainult tööpäevale", 2003);
        }
    }

    public static boolean isWorkday(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    public static void validateLunchOwnership(Integer userId, LunchEvent lunchEvent) {
        if (!lunchEvent.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sa saad muuda ainult enda loodud lõunaid", 2004);
        }
    }

    public static void validateLunchNotCanceled(LunchEvent lunchEvent) {
        if (Status.CANCELLED.getCode().equals(lunchEvent.getStatus())) {
            throw new ForbiddenException("Sa ei saa muuta lõunat, mis on tühistatud", 2005);
        }
    }

    public static void validateAvailableSpots(LunchEvent lunchEvent) {
        if (lunchEvent.getPaxAvailable() <= 0 || Status.FULL.getCode().equals(lunchEvent.getStatus())) {
            throw new ForbiddenException("Sellele lõunale ei ole vabu kohti", 2006);
        }
    }

    public static void validateSufficientSeats(int newPaxTotal, int currentJoinCount) {
        if (newPaxTotal < currentJoinCount) {
            throw new ForbiddenException("Sa ei saa vähendada lõunatajate koguarvu väiksemaks kui on liitujaid", 2010);
        }
    }
}