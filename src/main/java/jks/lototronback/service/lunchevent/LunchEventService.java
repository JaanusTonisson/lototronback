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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<LunchEvent> allAvailableLunches = lunchEventRepository.findAllAvailableLunches(Status.ACTIVE.getCode(), 0);
        return lunchEventMapper.toAvailableLunchEventDtos(allAvailableLunches);
    }
}
