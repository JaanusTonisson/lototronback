package jks.lototronback.service.lunchevent;

import jks.lototronback.controller.lunchevent.dto.LunchEventDto;
import jks.lototronback.persistence.lunchevent.LunchEventMapper;
import jks.lototronback.persistence.lunchevent.LunchEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LunchEventService {

    private final LunchEventRepository lunchEventRepository;
    private final LunchEventMapper lunchEventMapper;

    @Transactional
    public void addLunchEvent(LunchEventDto lunchEventDto) {

        //TODO: 1. loo lõuna - vali kuupäev, kellaaeg, koht, lisab kasutaja ID -> FRONDIST info
        //todo: tee andmebaasi jaoks söödavaks, mapi ära toEntity

        //TODO - liiguta see andmebaasi,

        //TODO - too andmebaasist lunch-eventi loomiseks vajalikud andmed
        //TODO - tee see dtoks, et front saaks kuvada

        //TODO - lunch event, mis kuvab frontii ja keegi saab liituma hakata


    }
}
