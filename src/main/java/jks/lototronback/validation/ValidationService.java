package jks.lototronback.validation;

import jks.lototronback.infrastructure.exception.DataNotFoundException;
import jks.lototronback.infrastructure.exception.ForbiddenException;
import jks.lototronback.persistence.lunchevent.LunchEvent;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static jks.lototronback.infrastructure.Error.*;

public class ValidationService {

    public static DataNotFoundException throwPrimaryKeyNotFoundException(String primaryKeyName, Integer value) {
        return new DataNotFoundException(PRIMARY_KEY_NOT_FOUND.getMessage() + primaryKeyName + " = " + value, PRIMARY_KEY_NOT_FOUND.getErrorCode());
    }

    public static DataNotFoundException throwForeignKeyNotFoundException(String fieldName, Integer value) {
        return new DataNotFoundException(FOREIGN_KEY_NOT_FOUND.getMessage() + fieldName + " = " + value, FOREIGN_KEY_NOT_FOUND.getErrorCode());
    }

    public static void validateLunchDateAndTime(LocalDate date, LocalTime time) {
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            throw new ForbiddenException(
                    LUNCH_IN_PAST.getMessage(),
                    LUNCH_IN_PAST.getErrorCode()
            );
        }

        if (date.equals(today) && time.isBefore(LocalTime.now())) {
            throw new ForbiddenException(
                    LUNCH_IN_PAST.getMessage(),
                    LUNCH_IN_PAST.getErrorCode()
            );
        }

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new ForbiddenException(
                    LUNCH_ON_WEEKEND.getMessage(),
                    LUNCH_ON_WEEKEND.getErrorCode()
            );
        }
    }

    public static void validateUserIsLunchCreator(Integer userId, LunchEvent lunchEvent) {
        if (!lunchEvent.getUser().getId().equals(userId)) {
            throw new ForbiddenException(
                    NOT_YOUR_LUNCH.getMessage(),
                    NOT_YOUR_LUNCH.getErrorCode()
            );
        }
    }

    public static void validateLunchNotCanceled(LunchEvent lunchEvent) {
        if ("C".equals(lunchEvent.getStatus())) {
            throw new ForbiddenException(
                    LUNCH_EVENT_CANCELED.getMessage(),
                    LUNCH_EVENT_CANCELED.getErrorCode()
            );
        }
    }

    public static void validateAtLeastOneLunchExists(List<LunchEvent> lunchEvents) {
        if (lunchEvents == null || lunchEvents.isEmpty()) {
            throw new DataNotFoundException(
                    NO_LUNCH_FOUND.getMessage(),
                    NO_LUNCH_FOUND.getErrorCode()
            );
        }
    }

    public static void validateAvailableSpots(LunchEvent lunchEvent) {
        if (lunchEvent.getPaxAvailable() <= 0) {
            throw new ForbiddenException(
                    LUNCH_NO_SPOTS.getMessage(),
                    LUNCH_NO_SPOTS.getErrorCode()
            );
        }
    }

    public static boolean isWorkday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

}

