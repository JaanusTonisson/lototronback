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
import jks.lototronback.service.message.MessageService;
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
    private final MessageService messageService;

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

        LunchEvent lunchEvent = lunchEventRepository.findById(lunchEventId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("lunchEventId", lunchEventId));

        ValidationService.validateLunchOwnership(userId, lunchEvent);

        ValidationService.validateLunchNotCanceled(lunchEvent);

        ValidationService.validateLunchDateAndTime(request.getDate(), request.getTime());

        if (!lunchEvent.getDate().equals(request.getDate()) || !lunchEvent.getTime().equals(request.getTime())) {
            validateTimeConflict(userId, request.getDate(), request.getTime());
        }

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("restaurantId", request.getRestaurantId()));

        int currentJoinCount = lunchEvent.getPaxTotal() - lunchEvent.getPaxAvailable();

        ValidationService.validateSufficientSeats(request.getPaxTotal(), currentJoinCount);

        lunchEvent.setRestaurant(restaurant);
        lunchEvent.setDate(request.getDate());
        lunchEvent.setTime(request.getTime());
        lunchEvent.setPaxTotal(request.getPaxTotal());
        lunchEvent.setPaxAvailable(request.getPaxTotal() - currentJoinCount);

        if (lunchEvent.getPaxAvailable() <= 0) {
            lunchEvent.setStatus(Status.FULL.getCode());
            lunchEvent.setIsAvailable(false);
        } else {
            lunchEvent.setStatus(Status.ACTIVE.getCode());
            lunchEvent.setIsAvailable(true);
        }

        lunchEventRepository.save(lunchEvent);

        LunchEventDto dto = lunchEventMapper.toLunchEventDto(lunchEvent);
        dto.setIsCreator(true);
        dto.setIsJoined(false);

        return dto;
    }

    @Transactional
    public void cancelLunchEvent(Integer userId, Integer lunchEventId) {

        LunchEvent lunchEvent = lunchEventRepository.findById(lunchEventId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("lunchEventId", lunchEventId));

        ValidationService.validateLunchOwnership(userId, lunchEvent);

        User creator = lunchEvent.getUser();

        lunchEvent.setStatus(Status.CANCELLED.getCode());
        lunchEvent.setIsAvailable(false);

        lunchEventRepository.save(lunchEvent);

        List<Register> registers = registerRepository.findByLunchEventId(lunchEventId);

        for (Register register : registers) {
            if (!register.getStatus().equals(Status.CANCELLED.getCode())) {
                messageService.createContactInfoMessage(
                        register.getUser(),      // joiner (receiver)
                        creator,                 // creator (sender)
                        lunchEvent,
                        "Lõuna on tühistatud",
                        "tühistas lõuna"
                );
            }
        }
    }

    @Transactional
    public LunchEventDto joinLunchEvent(Integer userId, Integer lunchEventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("userId", userId));

        LunchEvent lunchEvent = lunchEventRepository.findById(lunchEventId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("lunchEventId", lunchEventId));

        ValidationService.validateLunchNotCanceled(lunchEvent);

        ValidationService.validateAvailableSpots(lunchEvent);

        if (registerRepository.existsByUserIdAndLunchEventId(userId, lunchEventId)) {
            throw new ForbiddenException("Sa oled juba liitunud selle lõunaga", 2007);
        }

        if (lunchEvent.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sa oled selle lõuna looja", 2008);
        }

        Register register = new Register();
        register.setUser(user);
        register.setLunchEvent(lunchEvent);
        register.setStatus(Status.ACTIVE.getCode());

        registerRepository.save(register);

        lunchEvent.setPaxAvailable(lunchEvent.getPaxAvailable() - 1);

        if (lunchEvent.getPaxAvailable() <= 0) {
            lunchEvent.setStatus(Status.FULL.getCode());
            lunchEvent.setIsAvailable(false);
        }

        lunchEventRepository.save(lunchEvent);

        messageService.createContactInfoMessage(
                lunchEvent.getUser(),
                user,
                lunchEvent,
                "Keegi liitus sinu lõunaga.",
                "on liitunud sinu lõunaga"
        );

        messageService.createContactInfoMessage(
                user,
                lunchEvent.getUser(),
                lunchEvent,
                "Sa liitusid lõunaga",
                "on lõuna looja"
        );

        LunchEventDto dto = lunchEventMapper.toLunchEventDto(lunchEvent);
        dto.setIsCreator(false);
        dto.setIsJoined(true);

        return dto;
    }

    @Transactional
    public void cancelJoinedLunch(Integer userId, Integer lunchEventId) {

        LunchEvent lunchEvent = lunchEventRepository.findById(lunchEventId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("lunchEventId", lunchEventId));

        ValidationService.validateLunchNotCanceled(lunchEvent);

        Register register = registerRepository.findByUserIdAndLunchEventId(userId, lunchEventId)
                .orElseThrow(() -> new DataNotFoundException("Sa ei ole liitunud selle lõunaga", 2009));

        User user = register.getUser();

        register.setStatus(Status.CANCELLED.getCode());
        registerRepository.save(register);

        lunchEvent.setPaxAvailable(lunchEvent.getPaxAvailable() + 1);

        if (Status.FULL.getCode().equals(lunchEvent.getStatus()) && lunchEvent.getPaxAvailable() > 0) {
            lunchEvent.setStatus(Status.ACTIVE.getCode());
            lunchEvent.setIsAvailable(true);
        }

        lunchEventRepository.save(lunchEvent);

        messageService.createContactInfoMessage(
                lunchEvent.getUser(),
                user,
                lunchEvent,
                "Keegi lahkus su lõunalt",
                "lahkus su lõunalt"
        );
    }

    public List<LunchEventDto> getAvailableLunchesByDate(Integer userId, LocalDate date) {
        List<LunchEvent> lunchEvents = lunchEventRepository.findAvailableLunchesByDate(date);
        List<LunchEventDto> dtos = lunchEventMapper.toLunchEventDtos(lunchEvents);

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

        for (LunchEventDto dto : dtos) {
            dto.setIsCreator(false);
            dto.setIsJoined(true);
        }

        return dtos;
    }

    public void validateTimeConflict(Integer userId, LocalDate date, LocalTime time) {
        List<LunchEvent> createdLunches = lunchEventRepository.findByUserIdAndDate(userId, date);
        List<Register> joinedRegisters = registerRepository.findByUserIdAndLunchEventDate(userId, date);

        for (LunchEvent lunch : createdLunches) {
            LocalTime lunchTime = lunch.getTime();
            if (isTimeWithinInterval(time, lunchTime)) {
                throw new ForbiddenException("Sul juba on selles ajavahemikus lõuna tulemas", 2010);
            }
        }

        for (Register register : joinedRegisters) {
            LocalTime lunchTime = register.getLunchEvent().getTime();
            if (isTimeWithinInterval(time, lunchTime)) {
                throw new ForbiddenException("Sul juba on selles ajavahemikus lõuna tulemas", 2010);
            }
        }
    }

    private boolean isTimeWithinInterval(LocalTime time1, LocalTime time2) {
        long diffInMinutes = Math.abs(
                time1.toSecondOfDay() / 60 - time2.toSecondOfDay() / 60
        );
        return diffInMinutes < 60;
    }


    public List<LunchEventDto> getUserLunchesByDate(Integer userId, LocalDate date) {
        List<LunchEvent> createdLunches = lunchEventRepository.findByUserIdAndDate(userId, date);

        List<Register> joinedRegisters = registerRepository.findByUserIdAndLunchEventDate(userId, date);

        List<LunchEventDto> result = lunchEventMapper.toLunchEventDtos(createdLunches);

        List<LunchEvent> joinedLunches = joinedRegisters.stream()
                .map(Register::getLunchEvent)
                .toList();

        result.addAll(lunchEventMapper.toLunchEventDtos(joinedLunches));

        return result;
    }
}





