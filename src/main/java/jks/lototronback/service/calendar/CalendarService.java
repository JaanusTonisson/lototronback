package jks.lototronback.service.calendar;

import jks.lototronback.controller.lunchevent.dto.WorkdayCalendarDto;
import jks.lototronback.controller.lunchevent.dto.WorkdayDto;
import jks.lototronback.persistence.lunchevent.LunchEventRepository;
import jks.lototronback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final LunchEventRepository lunchEventRepository;

    public WorkdayCalendarDto getCurrentMonthWorkdayCalendar() {
        LocalDate today = LocalDate.now();
        return getWorkdayCalendarForMonth(today.getYear(), today.getMonthValue());
    }

    public WorkdayCalendarDto getWorkdayCalendarForMonth(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        WorkdayCalendarDto calendarDto = new WorkdayCalendarDto();
        calendarDto.setYear(year);
        calendarDto.setMonth(month);
        calendarDto.setWorkdays(generateWorkdaysForPeriod(firstDayOfMonth, lastDayOfMonth));

        return calendarDto;
    }

    private List<WorkdayDto> generateWorkdaysForPeriod(LocalDate startDate, LocalDate endDate) {
        List<WorkdayDto> workdays = new ArrayList<>();
        LocalDate currentDate = startDate;
        LocalDate today = LocalDate.now();

        while (!currentDate.isAfter(endDate)) {

            if (!currentDate.isBefore(today) && ValidationService.isWorkday(currentDate)) {
                WorkdayDto workdayDto = new WorkdayDto();
                workdayDto.setDate(currentDate);


                Integer availableLunchCount = lunchEventRepository.countAvailableLunchesByDate(currentDate);
                workdayDto.setHasAvailableLunches(availableLunchCount > 0);
                workdayDto.setAvailableLunchCount(availableLunchCount);

                workdays.add(workdayDto);
            }

            currentDate = currentDate.plusDays(1);
        }

        return workdays;
    }
}
