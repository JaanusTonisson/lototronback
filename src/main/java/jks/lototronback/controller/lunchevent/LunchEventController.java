package jks.lototronback.controller.lunchevent;

import io.swagger.v3.oas.annotations.Operation;
import jks.lototronback.controller.lunchevent.dto.AvailableEventDto;
import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.service.lunchevent.LunchEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class LunchEventController {

    private final LunchEventService lunchEventService;


    @PostMapping("/lunch-event")
    @Operation(summary = "Lisab uue lunch-eventi")
    public void addLunchEvent(@RequestBody LunchEventDto lunchEventDto) {
        lunchEventService.addLunchEvent(lunchEventDto);
    }

    @GetMapping("/lunch-events")
    @Operation(summary = "Toob kõik lunch-eventid andmebaasist, millega saab liituda (alates tänasest)",
                description = "Süsteemist otsitakse 3 parameetri alusel (is active, date alates tänasest " +
                        "ja pax available järgi) kõik loodud lõunad, millega saab liituda")
    public List<AvailableEventDto> getAvailableLunchEvents() {
        return lunchEventService.getAllAvailableLunchEvents();
    }

    @GetMapping("/user-added-events")
    @Operation(summary = "Toob kõik kasutaja loodud lunch-eventid andmebaasist (alates tänasest)",
                description = "Süsteemist otsitakse userId järgi ühe kasutaja loodud lõunad alates tänasest")
    public List<AvailableEventDto> getUserAddedLunchEvents(@RequestParam Integer userId) {
        return lunchEventService.getUserAddedLunchEvents(userId);
    }

    @GetMapping("/lunch-events-by-date")
    @Operation(summary = "Toob kõik ühe kuupäeva lunch-eventid andmevaasist, millega saab liituda",
    description = "Süsteemist otsitakse 3 parameetri alusel (is active, tänane kp, pax available)")
    public List<AvailableEventDto> getAllAvailableLunchesByDate(@RequestParam String nowDate) {
        return lunchEventService.getAllAvailableLunchesByDate(nowDate);
    }


    @GetMapping("/lunch-events-by-month")
    @Operation(summary =  "Toob ühe kuu kõik lunch-eventid andmebaasist, millega saab liituda",
                description = "Süsteemist otsitakse 3 parameetri alusel (date, is active, pax available)")
    public List<AvailableEventDto> getAllAvailableLunchesByMonth(@RequestParam String yearMonth) {
        return lunchEventService.getAllAvailableLunchesByMonth(yearMonth);
    }

    @GetMapping("/lunch-events/check-registration")
    @Operation(summary = "Toob ühe kasutajaga seotud lunch-eventid andmebaasist (mis tema loodud või kuhu ta liitunud)",
                description = "Süsteemist otsitakse 2 parameetri alusel (user id ja event id)")
    public List<AvailableEventDto> getAllUserEventRegistrations(@RequestParam Integer userId) {
        return lunchEventService.getAllUserEventRegistrations(userId);
    }


}
