package jks.lototronback.service.lunchevent;

import jks.lototronback.controller.lunchevent.dto.CreateLunchEventRequest;
import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.infrastructure.exception.ForbiddenException;
import jks.lototronback.persistence.lunchevent.LunchEvent;
import jks.lototronback.persistence.lunchevent.LunchEventMapper;
import jks.lototronback.persistence.lunchevent.LunchEventRepository;
import jks.lototronback.persistence.register.Register;
import jks.lototronback.persistence.register.RegisterRepository;
import jks.lototronback.persistence.restaurant.Restaurant;
import jks.lototronback.persistence.restaurant.RestaurantRepository;
import jks.lototronback.persistence.user.User;
import jks.lototronback.persistence.user.UserRepository;
import jks.lototronback.status.Status;
import jks.lototronback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static jks.lototronback.infrastructure.Error.*;

@Service
@RequiredArgsConstructor
public class LunchEventService {

    private final LunchEventRepository lunchEventRepository;
    private final RestaurantRepository restaurantRepository;
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;
    private final LunchEventMapper lunchEventMapper;

    @Transactional
    public LunchEventDto createLunchEvent(Integer userId, CreateLunchEventRequest request) {
        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("userId", userId));

        // Validate restaurant
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("restaurantId", request.getRestaurantId()));

        // Validate date and time
        ValidationService.validateLunchDateAndTime(request.getDate(), request.getTime());

        // Create lunch event
        LunchEvent lunchEvent = new LunchEvent();
        lunchEvent.setUser(user);
        lunchEvent.setRestaurant(restaurant);
        lunchEvent.setPaxTotal(request.getPaxTotal());
        lunchEvent.setPaxAvailable(request.getPaxTotal() - 1); // Creator is auto-joined
        lunchEvent.setDate(request.getDate());
        lunchEvent.setTime(request.getTime());
        lunchEvent.setStatus(Status.ACTIVE.getCode());
        lunchEvent.setIsAvailable(true);

        lunchEventRepository.save(lunchEvent);

        // Return DTO with additional fields
        LunchEventDto dto = lunchEventMapper.toLunchEventDto(lunchEvent);
        dto.setIsCreator(true);
        dto.setIsJoined(false);

        return dto;
    }

    @Transactional
    public LunchEventDto updateLunchEvent(Integer userId, Integer lunchEventId, CreateLunchEventRequest request) {
        // Get lunch event
        LunchEvent lunchEvent = lunchEventRepository.findById(lunchEventId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("lunchEventId", lunchEventId));

        // Verify ownership
        ValidationService.validateUserIsLunchCreator(userId, lunchEvent);

        // Validate not canceled
        ValidationService.validateLunchNotCanceled(lunchEvent);

        // Validate date and time
        ValidationService.validateLunchDateAndTime(request.getDate(), request.getTime());

        // Validate restaurant
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("restaurantId", request.getRestaurantId()));

        // Update lunch event
        lunchEvent.setRestaurant(restaurant);
        lunchEvent.setDate(request.getDate());
        lunchEvent.setTime(request.getTime());

        // Handle pax count changes
        int currentJoinCount = lunchEvent.getPaxTotal() - lunchEvent.getPaxAvailable();

        // Ensure new pax total is at least the current join count
        if (request.getPaxTotal() < currentJoinCount) {
            throw new ForbiddenException(
                    MIN_PAX_REQUIRED.getMessage(),
                    MIN_PAX_REQUIRED.getErrorCode()
            );
        }

        lunchEvent.setPaxTotal(request.getPaxTotal());
        lunchEvent.setPaxAvailable(request.getPaxTotal() - currentJoinCount);

        lunchEventRepository.save(lunchEvent);

        // Return DTO with additional fields
        LunchEventDto dto = lunchEventMapper.toLunchEventDto(lunchEvent);
        dto.setIsCreator(true);
        dto.setIsJoined(false);

        return dto;
    }

    @Transactional
    public void cancelLunchEvent(Integer userId, Integer lunchEventId) {
        // Get lunch event
        LunchEvent lunchEvent = lunchEventRepository.findById(lunchEventId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("lunchEventId", lunchEventId));

        // Verify ownership
        ValidationService.validateUserIsLunchCreator(userId, lunchEvent);

        // Update status
        lunchEvent.setStatus(Status.DEACTIVATED.getCode());
        lunchEvent.setIsAvailable(false);

        lunchEventRepository.save(lunchEvent);
    }

    @Transactional
    public LunchEventDto joinLunchEvent(Integer userId, Integer lunchEventId) {
        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("userId", userId));

        // Get lunch event
        LunchEvent lunchEvent = lunchEventRepository.findById(lunchEventId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("lunchEventId", lunchEventId));

        // Verify not canceled
        ValidationService.validateLunchNotCanceled(lunchEvent);

        // Check availability
        ValidationService.validateAvailableSpots(lunchEvent);

        // Check if user is already joined
        if (registerRepository.existsByUserIdAndLunchEventId(userId, lunchEventId)) {
            throw new ForbiddenException(
                    LUNCH_ALREADY_JOINED.getMessage(),
                    LUNCH_ALREADY_JOINED.getErrorCode()
            );
        }

        // Check if user is the creator
        if (lunchEvent.getUser().getId().equals(userId)) {
            throw new ForbiddenException(
                    LUNCH_EVENT_CREATOR.getMessage(),
                    LUNCH_EVENT_CREATOR.getErrorCode()
            );
        }

        // Create registration
        Register register = new Register();
        register.setUser(user);
        register.setLunchEvent(lunchEvent);
        registerRepository.save(register);

        // Update available spots
        lunchEvent.setPaxAvailable(lunchEvent.getPaxAvailable() - 1);

        // If full, set as unavailable
        if (lunchEvent.getPaxAvailable() <= 0) {
            lunchEvent.setIsAvailable(false);
        }

        lunchEventRepository.save(lunchEvent);

        // Return DTO with additional fields
        LunchEventDto dto = lunchEventMapper.toLunchEventDto(lunchEvent);
        dto.setIsCreator(false);
        dto.setIsJoined(true);

        return dto;
    }

    @Transactional
    public void cancelJoinedLunch(Integer userId, Integer lunchEventId) {
        // Get lunch event
        LunchEvent lunchEvent = lunchEventRepository.findById(lunchEventId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("lunchEventId", lunchEventId));

        // Check if already canceled
        ValidationService.validateLunchNotCanceled(lunchEvent);

        // Find registration
        Register register = registerRepository.findByUserIdAndLunchEventId(userId, lunchEventId)
                .orElseThrow(() -> new ForbiddenException(
                        "You have not joined this lunch event",
                        LUNCH_ALREADY_JOINED.getErrorCode()
                ));

        // Delete registration
        registerRepository.delete(register);

        // Update available spots
        lunchEvent.setPaxAvailable(lunchEvent.getPaxAvailable() + 1);

        // Make available again if it was full
        if (!lunchEvent.getIsAvailable() && lunchEvent.getPaxAvailable() > 0) {
            lunchEvent.setIsAvailable(true);
        }

        lunchEventRepository.save(lunchEvent);
    }

    public List<LunchEventDto> getAvailableLunchesByDate(Integer userId, LocalDate date) {
        List<LunchEvent> lunchEvents = lunchEventRepository.findAvailableLunchesByDate(date);
        List<LunchEventDto> dtos = lunchEventMapper.toLunchEventDtos(lunchEvents);

        // Set isCreator and isJoined flags
        for (LunchEventDto dto : dtos) {
            dto.setIsCreator(dto.getUserId().equals(userId));
            dto.setIsJoined(registerRepository.existsByUserIdAndLunchEventId(userId, dto.getId()));
        }

        return dtos;
    }

    public List<LunchEventDto> getUpcomingCreatedLunches(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<LunchEvent> lunchEvents = lunchEventRepository.findUpcomingLunchesByUserId(userId, today, now);
        List<LunchEventDto> dtos = lunchEventMapper.toLunchEventDtos(lunchEvents);

        // Set flags
        for (LunchEventDto dto : dtos) {
            dto.setIsCreator(true);
            dto.setIsJoined(false);
        }

        return dtos;
    }

    public List<LunchEventDto> getPastCreatedLunches(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<LunchEvent> lunchEvents = lunchEventRepository.findPastLunchesByUserId(userId, today, now);
        List<LunchEventDto> dtos = lunchEventMapper.toLunchEventDtos(lunchEvents);

        // Set flags
        for (LunchEventDto dto : dtos) {
            dto.setIsCreator(true);
            dto.setIsJoined(false);
        }

        return dtos;
    }

    public List<LunchEventDto> getUpcomingJoinedLunches(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Register> registers = registerRepository.findUpcomingJoinedLunchesByUserId(userId, today, now);
        List<LunchEvent> lunchEvents = registers.stream()
                .map(Register::getLunchEvent)
                .toList();

        List<LunchEventDto> dtos = lunchEventMapper.toLunchEventDtos(lunchEvents);

        // Set flags
        for (LunchEventDto dto : dtos) {
            dto.setIsCreator(false);
            dto.setIsJoined(true);
        }

        return dtos;
    }

    public List<LunchEventDto> getPastJoinedLunches(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Register> registers = registerRepository.findPastJoinedLunchesByUserId(userId, today, now);
        List<LunchEvent> lunchEvents = registers.stream()
                .map(Register::getLunchEvent)
                .toList();

        List<LunchEventDto> dtos = lunchEventMapper.toLunchEventDtos(lunchEvents);

        // Set flags
        for (LunchEventDto dto : dtos) {
            dto.setIsCreator(false);
            dto.setIsJoined(true);
        }

        return dtos;
    }


}







