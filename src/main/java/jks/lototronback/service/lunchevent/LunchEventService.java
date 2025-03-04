package jks.lototronback.service.lunchevent;

import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LunchEventService {

    @Transactional
    public void addLunchEvent(LunchEventDto lunchEventDto) {

    }
}
