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
    @Operation(summary = "Toob kõik loodud lunch-eventid andmebaasist")
    public List<AvailableEventDto> getAvailableLunchEvents() {
        return lunchEventService.getAllAvailableLunchEvents();
    }

    @GetMapping("/user-added-events")
    @Operation(summary = "Toob kasutaja loodud lunch-eventid andmebaasist")
    public List<AvailableEventDto> getUserAddedLunchEvents(@RequestParam Integer userId) {
        return lunchEventService.getUserAddedLunchEvents(userId);
    }

}
