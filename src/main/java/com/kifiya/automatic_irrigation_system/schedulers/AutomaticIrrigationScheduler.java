package com.kifiya.automatic_irrigation_system.schedulers;

import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.events.IrrigateLandEvent;
import com.kifiya.automatic_irrigation_system.repositories.LandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
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

    @Value("${reset_irrigation_status_timer:5}")
    private int resetIrrigationStatusTimer;

    Logger logger = LoggerFactory.getLogger(AutomaticIrrigationScheduler.class);

    @Scheduled(fixedRate = 60000)
    public void queryPlotsForIrrigation() {

        logger.debug("Checking against the database if there is land for automatic irrigation at this time");

        List<Land> plots = landRepository.findByScheduledIrrigationTimeGreaterThanEqualAndScheduledIrrigationTimeLessThanEqualAndIrrigationStatusAndStatus(
                LocalTime.now().minusSeconds(60), LocalTime.now().plusSeconds(60),
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

    /*
        We need to reset the `Irrigation Status` of every plot of land to ensure the next cycle
        of automatic irrigation the following day finds it in the state `PENDING_IRRIGATION` so it's eligible again
        for automatic irrigation
    */

    @Scheduled(fixedRate = 300000)
    public void resetIrrigationStatus()
    {
        try {
            logger.info("Starting process to reset Irrigation Status for all land automatically processed more than {} minutes ago", resetIrrigationStatusTimer);

            // For testing and simulation purposes, we will reset any land that was irrigated 5 minutes or more
            // ago to `PENDING_IRRIGATION`, to show this reset functionality indeed works
            List<Land> plots = landRepository.findByScheduledIrrigationTimeLessThan(LocalTime.now().minusMinutes(resetIrrigationStatusTimer));

            for(Land plot : plots)
                plot.setIrrigationStatus(Land.IrrigationStatus.PENDING_IRRIGATION);

            landRepository.saveAll(plots);

            logger.info("Finished resetting Irrigation Status for all lands that were scheduled for automatic irrigation more than {} minutes ago", resetIrrigationStatusTimer);
        }
        catch(Exception e) {
            logger.error("An error occurred while attempting to reset Irrigation Status for fields already irrigated, " +
                    "error details: {}", e.getMessage(), e);
        }
    }

}
