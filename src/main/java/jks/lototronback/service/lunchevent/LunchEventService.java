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
import jks.lototronback.persistence.user.User;
import jks.lototronback.service.message.MessageService;
import jks.lototronback.service.restaurant.RestaurantService;
import jks.lototronback.service.user.UserService;
import jks.lototronback.status.Status;
import jks.lototronback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LunchEventService {
    private final UserService userService;
    private final MessageService messageService;
    private final LunchEventMapper lunchEventMapper;
    private final RestaurantService restaurantService;
    private final RegisterRepository registerRepository;
    private final LunchEventRepository lunchEventRepository;

    @Transactional
    public LunchEventDto createLunchEvent(Integer userId, CreateLunchEventRequest createLunchEventRequest) {
        User user = getValidatedUser(userId);
        Restaurant restaurant = getValidatedRestaurant(createLunchEventRequest);
        validateLunchParameters(userId, createLunchEventRequest);
        LunchEvent lunchEvent = createAndSaveLunchEvent(createLunchEventRequest, user, restaurant);
        return createLunchEventDto(lunchEvent, true, false);
    }

    public List<LunchEventDto> getUserLunchesByDate(Integer userId, LocalDate date) {
        List<LunchEventDto> lunchEventDtos = lunchEventMapper.toLunchEventDtos(lunchEventRepository.findByUserIdAndDate(userId, date));
        List<LunchEvent> joinedLunches = registerRepository.findByUserIdAndLunchEventDate(userId, date).stream()
                .map(Register::getLunchEvent).toList();
        lunchEventDtos.addAll(lunchEventMapper.toLunchEventDtos(joinedLunches));
        return lunchEventDtos;
    }

    public List<LunchEventDto> getUpcomingCreatedLunches(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<LunchEvent> lunchEvents = lunchEventRepository.findUpcomingLunchesByUserId(userId, today, now);
        return createCreatorLunchEventDtos(lunchEvents);
    }

    public List<LunchEventDto> getPastCreatedLunches(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<LunchEvent> lunchEvents = lunchEventRepository.findPastLunchesByUserId(userId, today, now);
        return createCreatorLunchEventDtos(lunchEvents);
    }

    public List<LunchEventDto> getUpcomingJoinedLunches(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<Register> registers = registerRepository.findUpcomingJoinedLunchesByUserId(userId, today, now);
        List<LunchEvent> lunchEvents = registers.stream().map(Register::getLunchEvent).toList();
        return createJoinerLunchEventDtos(lunchEvents);
    }

    public List<LunchEventDto> getPastJoinedLunches(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<Register> registers = registerRepository.findPastJoinedLunchesByUserId(userId, today, now);
        List<LunchEvent> lunchEvents = registers.stream().map(Register::getLunchEvent).toList();
        return createJoinerLunchEventDtos(lunchEvents);
    }

    @Transactional
    public LunchEventDto updateLunchEvent(Integer userId, Integer lunchEventId, CreateLunchEventRequest createLunchEventRequest) {
        LunchEvent lunchEvent = getValidatedLunchEvent(lunchEventId);
        validateLunchForUpdate(userId, createLunchEventRequest, lunchEvent);
        Restaurant restaurant = getValidatedRestaurant(createLunchEventRequest);
        int currentPaxCount = calculateCurrentPaxCount(lunchEvent);
        validatePaxAvailable(createLunchEventRequest, currentPaxCount);
        updateAndSaveLunchEventDetails(createLunchEventRequest, lunchEvent, restaurant, currentPaxCount);
        return createLunchEventDto(lunchEvent, true, false);
    }

    @Transactional
    public void cancelJoinedLunch(Integer userId, Integer lunchEventId) {
        LunchEvent lunchEvent = getValidatedLunchEvent(lunchEventId);
        ValidationService.validateLunchNotCanceled(lunchEvent);
        Register register = getActiveRegisterForLunch(userId, lunchEventId);
        User leaver = register.getUser();
        markRegisterAsCancelled(register);
        increaseAvailableSpots(lunchEvent);
        updateLunchStatusAfterCancellation(lunchEvent);
        saveLunchEvent(lunchEvent);
        notifyCreatorAboutLeaving(leaver, lunchEvent);
    }

    @Transactional
    public LunchEventDto joinLunchEvent(Integer userId, Integer lunchEventId) {
        User user = getValidatedUser(userId);
        LunchEvent lunchEvent = getValidatedLunchEvent(lunchEventId);
        validateLunchForJoining(userId, lunchEventId, lunchEvent);
        Register register = createNewRegister(user, lunchEvent);
        saveRegister(register);
        decreaseAvailableSpots(lunchEvent);
        updateLunchAvailabilityStatus(lunchEvent);
        saveLunchEvent(lunchEvent);
        sendJoiningNotifications(user, lunchEvent);
        return createLunchEventDto(lunchEvent, false, true);
    }

    @Transactional
    public void cancelLunchEvent(Integer userId, Integer lunchEventId) {
        LunchEvent lunchEvent = getValidatedLunchEvent(lunchEventId);
        validateLunchOwnership(userId, lunchEvent);
        updateLunchEventStatusAsCancelled(lunchEvent);
        notifyCancellationToJoiners(lunchEventId, lunchEvent);
    }

    private void validateLunchParameters(Integer userId, CreateLunchEventRequest createLunchEventRequest) {
        ValidationService.validateLunchDateAndTime(createLunchEventRequest.getDate(), createLunchEventRequest.getTime());
        validateNoTimeConflicts(userId, createLunchEventRequest.getDate(), createLunchEventRequest.getTime());
    }

    private void validateNoTimeConflicts(Integer userId, LocalDate date, LocalTime time) {
        validateNoCreatedLunchTimeConflicts(userId, date, time);
        validateNoJoinedLunchTimeConflicts(userId, date, time);
    }

    private void validateNoCreatedLunchTimeConflicts(Integer userId, LocalDate date, LocalTime time) {
        List<LunchEvent> createdLunches = lunchEventRepository.findByUserIdAndDate(userId, date);

        for (LunchEvent lunch : createdLunches) {
            if (isTimeWithinConflictRange(time, lunch.getTime())) {
                throw new ForbiddenException("Sul juba on selles ajavahemikus lõuna tulemas", 2010);
            }
        }
    }

    private void validateNoJoinedLunchTimeConflicts(Integer userId, LocalDate date, LocalTime time) {
        List<Register> joinedRegisters = registerRepository.findByUserIdAndLunchEventDate(userId, date);

        for (Register register : joinedRegisters) {
            LocalTime lunchTime = register.getLunchEvent().getTime();
            if (isTimeWithinConflictRange(time, lunchTime)) {
                throw new ForbiddenException("Sul juba on selles ajavahemikus lõuna tulemas", 2010);
            }
        }
    }

    private LunchEvent createAndSaveLunchEvent(CreateLunchEventRequest createLunchEventRequest, User user, Restaurant restaurant) {
        LunchEvent lunchEvent = new LunchEvent();
        lunchEvent.setUser(user);
        lunchEvent.setRestaurant(restaurant);
        lunchEvent.setPaxTotal(createLunchEventRequest.getPaxTotal());
        lunchEvent.setPaxAvailable(createLunchEventRequest.getPaxTotal() - 1);
        lunchEvent.setDate(createLunchEventRequest.getDate());
        lunchEvent.setTime(createLunchEventRequest.getTime());
        lunchEvent.setStatus(Status.ACTIVE.getCode());
        lunchEvent.setIsAvailable(true);

        lunchEventRepository.save(lunchEvent);
        return lunchEvent;
    }

    private List<LunchEventDto> createLunchEventDtos(List<LunchEvent> lunchEvents, Integer userId) {
        List<LunchEventDto> lunchEventDtos = lunchEventMapper.toLunchEventDtos(lunchEvents);

        for (LunchEventDto lunchEventDto : lunchEventDtos) {
            lunchEventDto.setIsCreator(isUserCreator(userId, lunchEventDto.getUserId()));
            lunchEventDto.setIsJoined(isUserActivelyJoined(userId, lunchEventDto.getId()));
        }

        return lunchEventDtos;
    }

    private void updateAndSaveLunchEventDetails(CreateLunchEventRequest createLunchEventRequest, LunchEvent lunchEvent, Restaurant restaurant, int currentPaxCount) {
        lunchEvent.setRestaurant(restaurant);
        lunchEvent.setDate(createLunchEventRequest.getDate());
        lunchEvent.setTime(createLunchEventRequest.getTime());
        lunchEvent.setPaxTotal(createLunchEventRequest.getPaxTotal());
        lunchEvent.setPaxAvailable(createLunchEventRequest.getPaxTotal() - currentPaxCount);
        updateLunchEventAvailability(lunchEvent);
        lunchEventRepository.save(lunchEvent);
    }

    private static void updateLunchEventAvailability(LunchEvent lunchEvent) {
        if (lunchEvent.getPaxAvailable() <= 0) {
            lunchEvent.setStatus(Status.FULL.getCode());
            lunchEvent.setIsAvailable(false);
        } else {
            lunchEvent.setStatus(Status.ACTIVE.getCode());
            lunchEvent.setIsAvailable(true);
        }
    }

    private static void validatePaxAvailable(CreateLunchEventRequest createLunchEventRequest, int currentPaxCount) {
        ValidationService.validateSufficientSeats(createLunchEventRequest.getPaxTotal(), currentPaxCount);
    }

    private static int calculateCurrentPaxCount(LunchEvent lunchEvent) {
        return lunchEvent.getPaxTotal() - lunchEvent.getPaxAvailable();
    }

    private void validateLunchForUpdate(Integer userId, CreateLunchEventRequest createLunchEventRequest, LunchEvent lunchEvent) {
        validateLunchOwnership(userId, lunchEvent);
        ValidationService.validateLunchNotCanceled(lunchEvent);
        ValidationService.validateLunchDateAndTime(createLunchEventRequest.getDate(), createLunchEventRequest.getTime());

        if (!lunchEvent.getDate().equals(createLunchEventRequest.getDate()) || !lunchEvent.getTime().equals(createLunchEventRequest.getTime())) {
            validateNoTimeConflicts(userId, createLunchEventRequest.getDate(), createLunchEventRequest.getTime());
        }
    }

    private void notifyCancellationToJoiners(Integer lunchEventId, LunchEvent lunchEvent) {
        List<Register> registers = registerRepository.findByLunchEventId(lunchEventId);
        User creator = lunchEvent.getUser();

        for (Register register : registers) {
            if (!register.getStatus().equals(Status.CANCELLED.getCode())) {
                sendCancellationMessage(lunchEvent, register.getUser(), creator);
            }
        }
    }

    private void sendCancellationMessage(LunchEvent lunchEvent, User reciever, User sender) {
        messageService.createContactInfoMessage(
                reciever,
                sender,
                lunchEvent,
                "Lõuna on tühistatud",
                "tühistas lõuna"
        );
    }

    private void updateLunchEventStatusAsCancelled(LunchEvent lunchEvent) {
        lunchEvent.setStatus(Status.CANCELLED.getCode());
        lunchEvent.setIsAvailable(false);
        lunchEventRepository.save(lunchEvent);
    }

    private static void validateLunchOwnership(Integer userId, LunchEvent lunchEvent) {
        ValidationService.validateLunchOwnership(userId, lunchEvent);
    }

    private void validateLunchForJoining(Integer userId, Integer lunchEventId, LunchEvent lunchEvent) {
        ValidationService.validateLunchNotCanceled(lunchEvent);
        ValidationService.validateAvailableSpots(lunchEvent);
        validateUserNotAlreadyJoined(userId, lunchEventId);

        validateUserNotCreator(userId, lunchEvent);
    }

    private static void validateUserNotCreator(Integer userId, LunchEvent lunchEvent) {
        if (lunchEvent.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sa oled selle lõuna looja", 2008);
        }
    }

    private void validateUserNotAlreadyJoined(Integer userId, Integer lunchEventId) {
        if (isUserActivelyJoined(userId, lunchEventId)) {
            throw new ForbiddenException("Sa oled juba liitunud selle lõunaga", 2007);
        }
    }

    private boolean isUserActivelyJoined(Integer userId, Integer lunchEventId) {
        Optional<Register> register = registerRepository.findByUserIdAndLunchEventIdAndStatus(
                userId, lunchEventId, Status.ACTIVE.getCode());
        return register.isPresent();
    }

    private Register getActiveRegisterForLunch(Integer userId, Integer lunchEventId) {
        return registerRepository.findByUserIdAndLunchEventIdAndStatus(
                        userId, lunchEventId, Status.ACTIVE.getCode())
                .orElseThrow(() -> new DataNotFoundException("Sa ei ole liitunud selle lõunaga", 2009));
    }

    private boolean isUserCreator(Integer userId, Integer creatorId) {
        return creatorId.equals(userId);
    }

    private Register createNewRegister(User user, LunchEvent lunchEvent) {
        Register register = new Register();
        register.setUser(user);
        register.setLunchEvent(lunchEvent);
        register.setStatus(Status.ACTIVE.getCode());
        return register;
    }

    private void saveRegister(Register register) {
        registerRepository.save(register);
    }

    private void decreaseAvailableSpots(LunchEvent lunchEvent) {
        lunchEvent.setPaxAvailable(lunchEvent.getPaxAvailable() - 1);
    }

    private void increaseAvailableSpots(LunchEvent lunchEvent) {
        lunchEvent.setPaxAvailable(lunchEvent.getPaxAvailable() + 1);
    }

    private void sendJoiningNotifications(User joiner, LunchEvent lunchEvent) {
        notifyCreatorAboutJoining(joiner, lunchEvent);
        notifyJoinerAboutLunch(joiner, lunchEvent);
    }

    private void notifyCreatorAboutJoining(User joiner, LunchEvent lunchEvent) {
        User creator = lunchEvent.getUser();
        messageService.createContactInfoMessage(
                creator,
                joiner,
                lunchEvent,
                "Keegi liitus sinu lõunaga",
                "on liitunud sinu lõunaga"
        );
    }

    private void notifyJoinerAboutLunch(User joiner, LunchEvent lunchEvent) {
        User creator = lunchEvent.getUser();
        messageService.createContactInfoMessage(
                joiner,
                creator,
                lunchEvent,
                "Sa liitusid lõunaga",
                "on lõuna looja"
        );
    }

    private void markRegisterAsCancelled(Register register) {
        register.setStatus(Status.CANCELLED.getCode());
        saveRegister(register);
    }

    private void updateLunchStatusAfterCancellation(LunchEvent lunchEvent) {
        if (Status.FULL.getCode().equals(lunchEvent.getStatus()) && lunchEvent.getPaxAvailable() > 0) {
            lunchEvent.setStatus(Status.ACTIVE.getCode());
            lunchEvent.setIsAvailable(true);
        }
    }

    private void notifyCreatorAboutLeaving(User leaver, LunchEvent lunchEvent) {
        User creator = lunchEvent.getUser();
        messageService.createContactInfoMessage(
                creator,
                leaver,
                lunchEvent,
                "Keegi lahkus su lõunalt",
                "lahkus su lõunalt"
        );
    }

    public List<LunchEventDto> getAvailableLunchesByDate(Integer userId, LocalDate date) {
        List<LunchEvent> lunchEvents = lunchEventRepository.findAvailableLunchesByDate(date);
        return createLunchEventDtos(lunchEvents, userId);
    }

    private void updateLunchAvailabilityStatus(LunchEvent lunchEvent) {
        updateLunchEventAvailability(lunchEvent);
    }

    private void saveLunchEvent(LunchEvent lunchEvent) {
        lunchEventRepository.save(lunchEvent);
    }

    private User getValidatedUser(Integer userId) {
        return userService.getValidatedUser(userId);
    }

    private Restaurant getValidatedRestaurant(CreateLunchEventRequest createLunchEventRequest) {
        return restaurantService.getValidatedRestaurant(createLunchEventRequest.getRestaurantId());
    }

    private LunchEvent getValidatedLunchEvent(Integer lunchEventId) {
        return lunchEventRepository.findById(lunchEventId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("lunchEventId", lunchEventId));
    }

    private LunchEventDto createLunchEventDto(LunchEvent lunchEvent, boolean isCreator, boolean isJoined) {
        LunchEventDto lunchEventDto = lunchEventMapper.toLunchEventDto(lunchEvent);
        lunchEventDto.setIsCreator(isCreator);
        lunchEventDto.setIsJoined(isJoined);
        return lunchEventDto;
    }

    private List<LunchEventDto> createCreatorLunchEventDtos(List<LunchEvent> lunchEvents) {
        List<LunchEventDto> lunchEventDtos = lunchEventMapper.toLunchEventDtos(lunchEvents);
        for (LunchEventDto lunchEventDto : lunchEventDtos) {
            lunchEventDto.setIsCreator(true);
            lunchEventDto.setIsJoined(false);
        }
        return lunchEventDtos;
    }

    private List<LunchEventDto> createJoinerLunchEventDtos(List<LunchEvent> lunchEvents) {
        List<LunchEventDto> lunchEventDtos = lunchEventMapper.toLunchEventDtos(lunchEvents);
        for (LunchEventDto lunchEventDto : lunchEventDtos) {
            lunchEventDto.setIsCreator(false);
            lunchEventDto.setIsJoined(true);
        }
        return lunchEventDtos;
    }

    private boolean isTimeWithinConflictRange(LocalTime time1, LocalTime time2) {
        long diffInMinutes = Math.abs(time1.toSecondOfDay() / 60 - time2.toSecondOfDay() / 60);
        return diffInMinutes < 60;
    }




}





