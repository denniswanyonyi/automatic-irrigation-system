package com.kifiya.automatic_irrigation_system.schedulers;

import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.repositories.LandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class AutomaticIrrigationScheduler {

    @Autowired
    private LandRepository landRepository;

    @Scheduled(fixedRate = 1000)
    public void queryPlotsForIrrigation() {

        List<Land> plots = landRepository.findByScheduledIrrigationTime(LocalTime.now());

        if(plots == null || plots.isEmpty()) {

            return;
        }

        for(Land land : plots) {

        }
    }
}
