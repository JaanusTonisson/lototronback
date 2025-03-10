package jks.lototronback.service.lunchevent;

import jakarta.validation.constraints.NotNull;
import jks.lototronback.controller.lunchevent.dto.AvailableEventDto;
import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.persistence.lunchevent.LunchEvent;
import jks.lototronback.persistence.lunchevent.LunchEventMapper;
import jks.lototronback.persistence.lunchevent.LunchEventRepository;
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


    @Transactional
    public void addLunchEvent(@NotNull LunchEventDto lunchEventDto) {

        User user = userService.getValidatedUser(lunchEventDto.getUserId());

        Restaurant restaurant = restaurantService.getValidatedRestaurant(lunchEventDto.getRestaurantId());

        LunchEvent lunchEvent = lunchEventMapper.toLunchEvent(lunchEventDto);

        lunchEvent.setUser(user);
        lunchEvent.setRestaurant(restaurant);

        lunchEventRepository.save(lunchEvent);
    }

    public List<AvailableEventDto> getAllAvailableLunchEvents() {
        LocalDate nowDate = LocalDate.now();
        List<LunchEvent> allAvailableLunches = lunchEventRepository.findAllAvailableTodayAndFutureLunches(Status.ACTIVE.getCode(), nowDate, 0);
        return lunchEventMapper.toAvailableLunchEventDtos(allAvailableLunches);
    }

    public List<AvailableEventDto> getUserAddedLunchEvents(Integer userId) {
        LocalDate nowDate = LocalDate.now();
        List<LunchEvent> userCreatedTodayAndFutureLunches = lunchEventRepository.findUserCreatedTodayAndFutureLunches(userId, nowDate, Status.ACTIVE.getCode());
        return lunchEventMapper.toAvailableLunchEventDtos(userCreatedTodayAndFutureLunches);
    }

    public List<AvailableEventDto> getAllAvailableLunchesByDate(String nowDateString) {
        LocalDate nowDateLocal = LocalDate.parse(nowDateString);
        List<LunchEvent> allAvailableLunchesByDate = lunchEventRepository.findAllAvailableLunchesByDate(Status.ACTIVE.getCode(), nowDateLocal, 0);
        return lunchEventMapper.toAvailableLunchEventsByDate(allAvailableLunchesByDate);
    }

    public List<AvailableEventDto> getAllAvailableLunchesByMonth(String yearMonthStr) {
        YearMonth yearMonthObj = DateTimeConverter.stringToYearMonth(yearMonthStr);
        LocalDate firstDayOfMonth = yearMonthObj.atDay(1);
        List<LunchEvent> allAvailableLunchesByMonth = lunchEventRepository.findAllAvailableLunchesByMonth(firstDayOfMonth, 0, Status.ACTIVE.getCode());
        return lunchEventMapper.toAvailableLunchesByMonth(allAvailableLunchesByMonth);
    }

    public List<AvailableEventDto> getAllUserEventRegistrations(Integer userId) {
        List<LunchEvent> userEventRegistrations = lunchEventRepository.findAllUserEventRegistrations(0, 0);
        return lunchEventMapper.toUserEventRegistrations(userEventRegistrations);
    }
}







