package jks.lototronback.service.lunchevent;

import jks.lototronback.controller.lunchevent.dto.CreateLunchEventRequest;
import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.infrastructure.exception.DataNotFoundException;
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
import jks.lototronback.service.user.UserService;
import jks.lototronback.status.Status;
import jks.lototronback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LunchEventService {

    private final LunchEventRepository lunchEventRepository;
    private final RestaurantRepository restaurantRepository;
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;
    private final LunchEventMapper lunchEventMapper;
    private final UserService userService;

    @Transactional
    public LunchEventDto createLunchEvent(Integer userId, CreateLunchEventRequest request) {
        // Validate user
        User user = userService.getValidatedUser(userId);

        // Validate restaurant
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("restaurantId", request.getRestaurantId()));

        // Validate date and time
        ValidationService.validateLunchDateAndTime(request.getDate(), request.getTime());

        validateTimeConflict(userId, request.getDate(), request.getTime());

        // Create lunch event
        LunchEvent lunchEvent = new LunchEvent();
        lunchEvent.setUser(user);
        lunchEvent.setRestaurant(restaurant);
        lunchEvent.setPaxTotal(request.getPaxTotal());
        lunchEvent.setPaxAvailable(request.getPaxTotal() - 1);
        lunchEvent.setDate(request.getDate());
        lunchEvent.setTime(request.getTime());
        lunchEvent.setStatus(Status.ACTIVE.getCode());
        lunchEvent.setIsAvailable(true);

        lunchEventRepository.save(lunchEvent);

        // Convert to DTO and set additional flags
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
        ValidationService.validateLunchOwnership(userId, lunchEvent);

        // Validate not canceled
        ValidationService.validateLunchNotCanceled(lunchEvent);

        // Validate date and time
        ValidationService.validateLunchDateAndTime(request.getDate(), request.getTime());

        // If time or date changed, validate for conflicts
        if (!lunchEvent.getDate().equals(request.getDate()) || !lunchEvent.getTime().equals(request.getTime())) {
            validateTimeConflict(userId, request.getDate(), request.getTime());
        }

        // Validate restaurant
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("restaurantId", request.getRestaurantId()));

        // Calculate current join count
        int currentJoinCount = lunchEvent.getPaxTotal() - lunchEvent.getPaxAvailable();

        // Ensure new pax total is sufficient
        ValidationService.validateSufficientSeats(request.getPaxTotal(), currentJoinCount);

        // Update lunch event
        lunchEvent.setRestaurant(restaurant);
        lunchEvent.setDate(request.getDate());
        lunchEvent.setTime(request.getTime());
        lunchEvent.setPaxTotal(request.getPaxTotal());
        lunchEvent.setPaxAvailable(request.getPaxTotal() - currentJoinCount);

        // Update status if necessary
        if (lunchEvent.getPaxAvailable() <= 0) {
            lunchEvent.setStatus(Status.FULL.getCode());
            lunchEvent.setIsAvailable(false);
        } else {
            lunchEvent.setStatus(Status.ACTIVE.getCode());
            lunchEvent.setIsAvailable(true);
        }

        lunchEventRepository.save(lunchEvent);

        // Convert to DTO and set additional flags
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
        ValidationService.validateLunchOwnership(userId, lunchEvent);

        // Update status
        lunchEvent.setStatus(Status.CANCELLED.getCode());
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
            throw new ForbiddenException("You have already joined this lunch event", 2007);
        }

        // Check if user is the creator
        if (lunchEvent.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You are the creator of this lunch event", 2008);
        }

        // Create registration
        Register register = new Register();
        register.setUser(user);
        register.setLunchEvent(lunchEvent);
        register.setStatus(Status.ACTIVE.getCode()); // Set status for registration

        registerRepository.save(register);

        // Update available spots
        lunchEvent.setPaxAvailable(lunchEvent.getPaxAvailable() - 1);

        // If full, update status
        if (lunchEvent.getPaxAvailable() <= 0) {
            lunchEvent.setStatus(Status.FULL.getCode());
            lunchEvent.setIsAvailable(false);
        }

        lunchEventRepository.save(lunchEvent);

        // Convert to DTO and set additional flags
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
                .orElseThrow(() -> new DataNotFoundException("You have not joined this lunch event", 2009));

        // Update registration status
        register.setStatus(Status.CANCELLED.getCode());
        registerRepository.save(register);

        // Update available spots
        lunchEvent.setPaxAvailable(lunchEvent.getPaxAvailable() + 1);

        // Update lunch event status if it was full
        if (Status.FULL.getCode().equals(lunchEvent.getStatus()) && lunchEvent.getPaxAvailable() > 0) {
            lunchEvent.setStatus(Status.ACTIVE.getCode());
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

    public void validateTimeConflict(Integer userId, LocalDate date, LocalTime time) {
        // Get all lunches (both created and joined) for this user on this date
        List<LunchEvent> createdLunches = lunchEventRepository.findByUserIdAndDate(userId, date);
        List<Register> joinedRegisters = registerRepository.findByUserIdAndLunchEventDate(userId, date);

        // Check created lunches for time conflicts
        for (LunchEvent lunch : createdLunches) {
            LocalTime lunchTime = lunch.getTime();
            if (isTimeWithinInterval(time, lunchTime, 60)) {
                throw new ForbiddenException("You already have a lunch scheduled within 60 minutes of this time", 2010);
            }
        }

        // Check joined lunches for time conflicts
        for (Register register : joinedRegisters) {
            LocalTime lunchTime = register.getLunchEvent().getTime();
            if (isTimeWithinInterval(time, lunchTime, 60)) {
                throw new ForbiddenException("You already have a lunch scheduled within 60 minutes of this time", 2010);
            }
        }
    }

    private boolean isTimeWithinInterval(LocalTime time1, LocalTime time2, int minutes) {
        long diffInMinutes = Math.abs(
                time1.toSecondOfDay() / 60 - time2.toSecondOfDay() / 60
        );
        return diffInMinutes < minutes;
    }


    public List<LunchEventDto> getUserLunchesByDate(Integer userId, LocalDate date) {
        // Get created lunches for the user on this date
        List<LunchEvent> createdLunches = lunchEventRepository.findByUserIdAndDate(userId, date);

        // Get joined lunches for the user on this date
        List<Register> joinedRegisters = registerRepository.findByUserIdAndLunchEventDate(userId, date);

        // Convert created lunches to DTOs
        List<LunchEventDto> result = lunchEventMapper.toLunchEventDtos(createdLunches);

        // Convert joined lunches to DTOs and add them to the result
        List<LunchEvent> joinedLunches = joinedRegisters.stream()
                .map(Register::getLunchEvent)
                .toList();

        // Add joined lunches to result
        result.addAll(lunchEventMapper.toLunchEventDtos(joinedLunches));

        return result;
    }
}





