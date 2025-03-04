package jks.lototronback.controller.lunchevent;

import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.service.lunchevent.LunchEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LunchEventController {

    private final LunchEventService lunchEventService;

    @PostMapping("/lunch-event")
    public void addLunchEvent(@RequestBody LunchEventDto lunchEventDto) {
        lunchEventService.addLunchEvent(lunchEventDto);
    }

}
