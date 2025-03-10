package jks.lototronback.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;

public class DateTimeConverter {
    public static LocalTime stringToLocalTimeArray(String timeAsString) {
        String[] timeSplitArray = timeAsString.split(":");
        String hourAsString = timeSplitArray[0];
        String minutesAsString = timeSplitArray[1];
        int hour = Integer.parseInt(hourAsString);
        int minutes = Integer.parseInt(minutesAsString);
        return LocalTime.of(hour, minutes);
    }

    public static LocalDate stringToLocalDateArray(String dateAsString) {
        String[] dateSplitArray = dateAsString.split("-");
        String yearAsString = dateSplitArray[0];
        String monthAsString = dateSplitArray[1];
        String dayAsString = dateSplitArray[2];

        int year = Integer.parseInt(yearAsString);
        int month = Integer.parseInt(monthAsString);
        int day = Integer.parseInt(dayAsString);
        return LocalDate.of(year, month, day);
    }

    public static YearMonth getYearMonthFromLocalDate(LocalDate date) {
        return YearMonth.from(date);
    }
    public static YearMonth stringToYearMonth(String yearMonthStr) {
        String[] parts = yearMonthStr.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        return YearMonth.of(year, month);
    }

    }

