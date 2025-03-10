package jks.lototronback.controller.lunchevent;

import io.swagger.v3.oas.annotations.Operation;
import jks.lototronback.controller.lunchevent.dto.AvailableEventDto;
import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.service.lunchevent.LunchEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/lunch-events")
    @Operation(summary = "Toob kõik ühe kuupäeva lunc-eventid andmevaasist, millega saab liituda",
    description = "Süsteemist otsitakse 3 parameetri alusel (is active, tänane kp, pax available)")
    public List<AvailableEventDto> getAllAvailableLunchesByDate(@RequestParam String nowDateString) {
        return lunchEventService.getAllAvailableLunchesByDate(nowDateString);
    }

}
