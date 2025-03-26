package jks.lototronback.controller.lunchevent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jks.lototronback.controller.lunchevent.dto.CreateLunchEventRequest;
import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.service.lunchevent.LunchEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class LunchEventController {

    private final LunchEventService lunchEventService;

    @PostMapping("/lunch/event")
    @Operation(summary = "Loob uue lõuna")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lõuna edukalt loodud"),
            @ApiResponse(responseCode = "403", description = "Valideerimine ebaõnnestus"),
            @ApiResponse(responseCode = "404", description = "Required entity not found")
    })
    public LunchEventDto createLunchEvent(
            @RequestParam Integer userId,
            @RequestBody CreateLunchEventRequest createLunchEventRequest) {
        return lunchEventService.createLunchEvent(userId, createLunchEventRequest);
    }

    @PutMapping("/lunch/event/{lunchEventId}")
    @Operation(summary = "Uuendab olemasolevat lõunat")
    public LunchEventDto updateLunchEvent(
            @RequestParam Integer userId,
            @PathVariable Integer lunchEventId,
            @RequestBody CreateLunchEventRequest request) {
        return lunchEventService.updateLunchEvent(userId, lunchEventId, request);
    }

    @DeleteMapping("/lunch/event/{lunchEventId}")
    @Operation(summary = "Tühistab olemasolevat lõuna")
    public void cancelLunchEvent(
            @RequestParam Integer userId,
            @PathVariable Integer lunchEventId) {
        lunchEventService.cancelLunchEvent(userId, lunchEventId);
    }

    @PostMapping("/lunch/event/{lunchEventId}/join")
    @Operation(summary = "Liitub loodud lõunaga")
    public LunchEventDto joinLunchEvent(
            @RequestParam Integer userId,
            @PathVariable Integer lunchEventId) {
        return lunchEventService.joinLunchEvent(userId, lunchEventId);
    }

    @DeleteMapping("/lunch/event/{lunchEventId}/join")
    @Operation(summary = "Loobub lõunal osalemisest")
    public void cancelJoinedLunch(
            @RequestParam Integer userId,
            @PathVariable Integer lunchEventId) {
        lunchEventService.cancelJoinedLunch(userId, lunchEventId);
    }

    @GetMapping("/lunch/events/available")
    @Operation(summary = "Toob lõunad valitud päevale")
    public List<LunchEventDto> getAvailableLunchesByDate(
            @RequestParam Integer userId,
            @RequestParam LocalDate date) {
        return lunchEventService.getAvailableLunchesByDate(userId, date);
    }

    @GetMapping("/lunch/events/upcoming/created")
    @Operation(summary = "Toob eesolevad lõunad, mille kasutaja on loonud")
    public List<LunchEventDto> getUpcomingCreatedLunches(
            @RequestParam Integer userId) {
        return lunchEventService.getUpcomingCreatedLunches(userId);
    }

    @GetMapping("/lunch/events/past/created")
    @Operation(summary = "Toob minevikus toimunud lõunad, mille kasutaja on loonud")
    public List<LunchEventDto> getPastCreatedLunches(
            @RequestParam Integer userId) {
        return lunchEventService.getPastCreatedLunches(userId);
    }

    @GetMapping("/lunch/events/upcoming/joined")
    @Operation(summary = "Toob eesolevad lõunad, millega kasutaja on liitunud")
    public List<LunchEventDto> getUpcomingJoinedLunches(
            @RequestParam Integer userId) {
        return lunchEventService.getUpcomingJoinedLunches(userId);
    }

    @GetMapping("/lunch/events/past/joined")
    @Operation(summary = "Toob toimunud lõunad, millga kasutaja on liitunud")
    public List<LunchEventDto> getPastJoinedLunches(
            @RequestParam Integer userId) {
        return lunchEventService.getPastJoinedLunches(userId);
    }

    @GetMapping("/lunch/events/user-date")
    @Operation(summary = "Toob kasutaja lõunad valitud kuupäeval")
    public List<LunchEventDto> getUserLunchesByDate(
            @RequestParam Integer userId,
            @RequestParam LocalDate date) {
        return lunchEventService.getUserLunchesByDate(userId, date);
    }
}