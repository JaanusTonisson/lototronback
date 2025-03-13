package jks.lototronback.controller.calendar;

import io.swagger.v3.oas.annotations.Operation;
import jks.lototronback.controller.lunchevent.dto.WorkdayCalendarDto;
import jks.lototronback.service.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/calendar/current")
    @Operation(summary = "Too tööpäevade kalender praeguse kuu kohta")
    public WorkdayCalendarDto getCurrentMonthWorkdayCalendar() {
        return calendarService.getCurrentMonthWorkdayCalendar();
    }

    @GetMapping("/calendar")
    @Operation(summary = "Get workday calendar for specified month")
    public WorkdayCalendarDto getWorkdayCalendarForMonth(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        return calendarService.getWorkdayCalendarForMonth(year, month);
    }
}
