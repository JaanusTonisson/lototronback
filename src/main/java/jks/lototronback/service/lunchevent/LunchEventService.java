package jks.lototronback.service.lunchevent;

import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.persistence.lunchevent.LunchEvent;
import jks.lototronback.persistence.lunchevent.LunchEventMapper;
import jks.lototronback.persistence.lunchevent.LunchEventRepository;
import jks.lototronback.persistence.user.User;
import jks.lototronback.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LunchEventService {

    private final LunchEventRepository lunchEventRepository;
    private final LunchEventMapper lunchEventMapper;
    private final UserService userService;

    @Transactional
    public void addLunchEvent(LunchEventDto lunchEventDto) {
        User user = userService.getValidatedUser(lunchEventDto.getUserId());
        LunchEvent lunchEvent = lunchEventMapper.toLunchEvent(lunchEventDto);
        lunchEvent.setUser(user);
        //TODO: võta kokku kõik andmed dtost
        //TODO:
    }
}
