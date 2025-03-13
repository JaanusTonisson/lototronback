package jks.lototronback.service.lunchevent;

import jakarta.validation.constraints.NotNull;
import jks.lototronback.controller.lunchevent.dto.AvailableEventDto;
import jks.lototronback.controller.lunchevent.dto.JoinLunchDto;
import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.persistence.lunchevent.LunchEvent;
import jks.lototronback.persistence.lunchevent.LunchEventMapper;
import jks.lototronback.persistence.lunchevent.LunchEventRepository;
import jks.lototronback.persistence.register.Register;
import jks.lototronback.persistence.register.RegisterRepository;
import jks.lototronback.persistence.restaurant.Restaurant;
import jks.lototronback.persistence.user.User;
import jks.lototronback.service.restaurant.RestaurantService;
import jks.lototronback.service.user.UserService;
import jks.lototronback.status.Status;
import jks.lototronback.util.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LunchEventService {

    private final LunchEventRepository lunchEventRepository;
    private final LunchEventMapper lunchEventMapper;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final RegisterRepository registerRepository;


    @Transactional
    public void addLunchEvent(@NotNull LunchEventDto lunchEventDto) {

        User user = userService.getValidatedUser(lunchEventDto.getUserId());

        Restaurant restaurant = restaurantService.getValidatedRestaurant(lunchEventDto.getRestaurantId());

        LunchEvent lunchEvent = lunchEventMapper.toLunchEvent(lunchEventDto);

        lunchEvent.setUser(user);
        lunchEvent.setRestaurant(restaurant);

        lunchEventRepository.save(lunchEvent);

        Register register = new Register();
        register.setUser(user);
        register.setLunchEvent(lunchEvent);
        register.setStatus(Status.ACTIVE.getCode());
        registerRepository.save(register);

    }

    public List<AvailableEventDto> getAllAvailableLunchEvents() {
        LocalDate nowDate = LocalDate.now();
        List<LunchEvent> allAvailableLunches = lunchEventRepository.findAllAvailableTodayAndFutureLunches(Status.ACTIVE.getCode(), nowDate);
        return lunchEventMapper.toAvailableLunchEventDtos(allAvailableLunches);
    }

    public List<AvailableEventDto> getUserAddedLunchEvents(Integer userId) {
        LocalDate nowDate = LocalDate.now();
        List<LunchEvent> userCreatedTodayAndFutureLunches = lunchEventRepository.findUserCreatedTodayAndFutureLunches(userId, nowDate, Status.ACTIVE.getCode());
        return lunchEventMapper.toAvailableLunchEventDtos(userCreatedTodayAndFutureLunches);
    }

    public List<AvailableEventDto> getAllAvailableLunchesByDate(String nowDateString) {
        LocalDate nowDateLocal = LocalDate.parse(nowDateString);
        List<LunchEvent> allAvailableLunchesByDate = lunchEventRepository.findAllAvailableLunchesByDate(Status.ACTIVE.getCode(), nowDateLocal);
        return lunchEventMapper.toAvailableLunchEventDtos(allAvailableLunchesByDate);
    }

    public List<AvailableEventDto> getAllAvailableLunchesByMonth(String yearMonthStr) {
        YearMonth yearMonthObj = DateTimeConverter.stringToYearMonth(yearMonthStr);
        List<LunchEvent> allAvailableLunchesByMonth = lunchEventRepository.findAllAvailableLunchesByMonth(Status.ACTIVE.getCode(), yearMonthObj.getYear(), yearMonthObj.getMonthValue());
        return lunchEventMapper.toAvailableLunchEventDtos(allAvailableLunchesByMonth);
    }

    public List<AvailableEventDto> getAllUserEventRegistrations(Integer userId) {
        LocalDate nowDate = LocalDate.now();
        List<LunchEvent> userEventRegistrations = lunchEventRepository.findAllUserEventRegistrations(userId, nowDate);
        return lunchEventMapper.toAvailableLunchEventDtos(userEventRegistrations);
    }

    public List<AvailableEventDto> getUserAddedAndRegisteredLunches(Integer userId) {
        List<AvailableEventDto> userLunches = getUserAddedLunchEvents(userId);
        userLunches.addAll(getAllUserEventRegistrations(userId));
        return userLunches;
    }

    @Transactional
    public void joinLunch(JoinLunchDto joinLunchDto) {
        Integer userId = joinLunchDto.getUserId();
        Integer eventId = joinLunchDto.getEventId();

        User user = userService.getValidatedUser(joinLunchDto.getUserId());

        LunchEvent lunchEvent = lunchEventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Ei leitud lõunat ID: " + eventId));

        if (registerRepository.existsByUserIdAndLunchEventId(userId, eventId)) {
            throw new IllegalArgumentException("Kasutaja on juba registreerunud sellele lõunale.");
        }
        lunchEvent.setPaxAvailable(lunchEvent.getPaxAvailable() - 1);
        Register register = new Register();
        register.setUser(user);
        registerRepository.save(register);
    }

}







