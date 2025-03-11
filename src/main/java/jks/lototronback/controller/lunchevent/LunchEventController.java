package jks.lototronback.controller.lunchevent;

import io.swagger.v3.oas.annotations.Operation;
import jks.lototronback.controller.lunchevent.dto.AvailableEventDto;
import jks.lototronback.controller.lunchevent.dto.JoinLunchDto;
import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.persistence.register.RegisterRepository;
import jks.lototronback.service.lunchevent.LunchEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class LunchEventController {

    private final LunchEventService lunchEventService;
    private final RegisterRepository registerRepository;


    @PostMapping("/lunch-event")
    @Operation(summary = "Lisab uue lunch-eventi.")
    public void addLunchEvent(@RequestBody LunchEventDto lunchEventDto) {
        lunchEventService.addLunchEvent(lunchEventDto);
    }

    @GetMapping("/lunch-events")
    @Operation(summary = "Kõik lunch-eventid, millega saab liituda.",
            description = "Süsteemist otsitakse 3 parameetri alusel (is active, date alates tänasest " +
                    "ja pax available järgi) kõik loodud lõunad, millega saab liituda.")
    public List<AvailableEventDto> getAvailableLunchEvents() {
        return lunchEventService.getAllAvailableLunchEvents();
    }

    @GetMapping("/user-added-events")
    @Operation(summary = "Kõik kasutaja loodud lunch-eventid (alates tänasest).",
            description = "Süsteemist otsitakse userId järgi ühe kasutaja loodud lõunad alates tänasest.")
    public List<AvailableEventDto> getUserAddedLunchEvents(@RequestParam Integer userId) {
        return lunchEventService.getUserAddedLunchEvents(userId);
    }

    @GetMapping("/lunch-events-by-date")
    @Operation(summary = "Ühe kuupäeva lõikes lunch-eventid, millega saab liituda.",
            description = "Süsteemist otsitakse 3 parameetri alusel (is active, tänane kp, pax available).")
    public List<AvailableEventDto> getAllAvailableLunchesByDate(@RequestParam String nowDate) {
        return lunchEventService.getAllAvailableLunchesByDate(nowDate);
    }

    @GetMapping("/lunch-events-by-month")
    @Operation(summary = "Ühe kuu lõikes lunch-eventid, millega saab liituda.",
            description = "Süsteemist otsitakse 3 parameetri alusel (date, is active, pax available). " +
                    "Kuvatakse alates tänasest.")
    public List<AvailableEventDto> getAllAvailableLunchesByMonth(@RequestParam String yearMonth) {
        return lunchEventService.getAllAvailableLunchesByMonth(yearMonth);
    }

    @GetMapping("/lunch-events/check-registration")
    @Operation(summary = "Kasutaja lunch-eventid, kuhu ta on liitunud",
            description = "Süsteemist otsitakse register tabelist 2 parameetri alusel (user id ja lunch event id). " +
                    "Kuvatakse alates tänasest.")
    public List<AvailableEventDto> getAllUserEventRegistrations(@RequestParam Integer userId) {
        return lunchEventService.getAllUserEventRegistrations(userId);
    }

    @GetMapping("/lunch-events/added-and-registered")
    @Operation(summary = "Kasutaja lõunad, mille ta on loonud ja millega liitunud.",
            description = "Liidetakse kokku lõunad, mille kasutaja on loonud ja lõunad, millega ta on liitunud. " +
                    "Kuvatakse alates tänasest.")
    public List<AvailableEventDto> getUserAddedAndRegisteredLunches(@RequestParam Integer userId) {
        return lunchEventService.getUserAddedAndRegisteredLunches(userId);
    }

    @PostMapping("/lunch-event/join")
    @Operation(summary = "Liitumine lunch-eventiga",
    description = "")

    public ResponseEntity<String> joinLunchEvent(@RequestBody JoinLunchDto joinLunchDto) {
        try {
            lunchEventService.joinLunch(joinLunchDto);
            return ResponseEntity.ok("Liitumine lõunaga õnnestus.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // 400 Bad Request for invalid data
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Liitumine lõunaga ebaõnnestus: " + e.getMessage()); // 500 Server Error
        }
    }

}
