package com.kifiya.automatic_irrigation_system.schedulers;

import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.events.IrrigateLandEvent;
import com.kifiya.automatic_irrigation_system.repositories.LandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class AutomaticIrrigationScheduler {

    @Autowired
    private LandRepository landRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    Logger logger = LoggerFactory.getLogger(AutomaticIrrigationScheduler.class);

    @Scheduled(fixedRate = 1000)
    public void queryPlotsForIrrigation() {

        logger.debug("Checking against the database if there is land for automatic irrigation at this time");

        List<Land> plots = landRepository.findByScheduledIrrigationTimeGreaterThanEqualAndScheduledIrrigationTimeLessThanEqualAndIrrigationStatusAndStatus(
                LocalTime.now().minusSeconds(30), LocalTime.now().plusSeconds(30),
                Land.IrrigationStatus.PENDING_IRRIGATION, Land.LandStatus.ACTIVE);

        logger.info("Found {} plots of land for automatic irrigation", plots.size());

        if(plots.isEmpty()) {

            logger.debug("No plots found for automatic irrigation at this time");

            return;
        }

        // IF we reach here, it means we came across plot(s) of land that are elligible for irrigation
        // at this minute of the hour
        logger.info("{} plots of land found for automatic irrigation at this time", plots.size());

        for(Land land : plots) {
            logger.info("Sending plot of land info to queue to notify sensor to irrigate it, plot info is: {}", land.toString());

            eventPublisher.publishEvent(new IrrigateLandEvent(this, land));
        }
    }
}
