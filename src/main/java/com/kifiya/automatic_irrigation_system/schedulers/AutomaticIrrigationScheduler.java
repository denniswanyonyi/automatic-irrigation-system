package com.kifiya.automatic_irrigation_system.schedulers;

import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.events.IrrigateLandEvent;
import com.kifiya.automatic_irrigation_system.repositories.LandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class AutomaticIrrigationScheduler {

    @Autowired
    private LandRepository landRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 1000)
    public void queryPlotsForIrrigation() {

        List<Land> plots = landRepository.findByScheduledIrrigationTime(LocalTime.now());

        if(plots == null || plots.isEmpty()) {

            return;
        }

        // IF we reach here, it means we came across plot(s) of land that are elligible for irrigation
        // at this minute of the hour

        for(Land land : plots) {
            eventPublisher.publishEvent(new IrrigateLandEvent(this, land));
        }
    }
}
