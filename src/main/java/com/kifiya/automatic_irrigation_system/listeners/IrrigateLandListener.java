package com.kifiya.automatic_irrigation_system.listeners;

import com.kifiya.automatic_irrigation_system.dao.Alerts;
import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.dto.ApiResponse;
import com.kifiya.automatic_irrigation_system.dto.LandInfo;
import com.kifiya.automatic_irrigation_system.events.IrrigateLandEvent;
import com.kifiya.automatic_irrigation_system.repositories.AlertsRepository;
import com.kifiya.automatic_irrigation_system.repositories.LandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class IrrigateLandListener  implements ApplicationListener<IrrigateLandEvent> {

    @Autowired
    LandRepository landRepository;

    @Autowired
    AlertsRepository alertsRepository;

    @Value("${sensor_url}")
    private String sensorURL;

    @Value("${max_retries}")
    private int maxRetries;

    Logger logger = LoggerFactory.getLogger(IrrigateLandListener.class);

    @Async
    @Override
    public void onApplicationEvent(IrrigateLandEvent irrigateLandEvent)
    {
        logger.info("Received the following info to irrigate the following plot of land: {}", irrigateLandEvent.getLand().toString());

        RestTemplate restTemplate = new RestTemplate();

        LandInfo landInfo = new LandInfo(irrigateLandEvent.getLand().getId(),
                irrigateLandEvent.getLand().getLength(), irrigateLandEvent.getLand().getWidth(),
                irrigateLandEvent.getLand().getArea(), irrigateLandEvent.getLand().getCrop(),
                irrigateLandEvent.getLand().getAmountOfWater(), irrigateLandEvent.getLand().getStatus(),
                irrigateLandEvent.getLand().getIrrigationStatus(), irrigateLandEvent.getLand().getDateAdded(),
                irrigateLandEvent.getLand().getLastIrrigated(),
                irrigateLandEvent.getLand().getScheduledIrrigationTime());


        ResponseEntity<ApiResponse> responseEntity = null;
        int totalRetries = 0;

        while(totalRetries < maxRetries && responseEntity == null) {

            try {
                totalRetries += 1;
                responseEntity = restTemplate.postForEntity(sensorURL, landInfo, ApiResponse.class);
            }
            catch(Exception e) {
                // Catch the exception and allow another retry until we reach the required retries

                logger.error("An error occurred when calling sensor, this is for request #{}, the error is: {}", totalRetries, e.getMessage());
            }
        }

        try {
            if (responseEntity.getBody() != null && responseEntity.getStatusCode().value() == 201
                    && responseEntity.getBody().getResponseCode() != null
                    && responseEntity.getBody().getResponseCode().equals("0")) {

                irrigateLandEvent.getLand().setLastIrrigated(LocalDateTime.now());
                irrigateLandEvent.getLand().setIrrigationStatus(Land.IrrigationStatus.IRRIGATION_SUCCESSFUL);

                landRepository.save(irrigateLandEvent.getLand());

                logger.info("Successfully notified sensor to irrigate land with the following info: {}", landInfo.toString());
            } else {

                // Save this as we will need to issue an alert
                alertsRepository.save(new Alerts(
                        LocalDateTime.now(), "Exceeded max retries notifying sensor to trigger automatic irrigation", null,
                        "Max Retry Exceeded for Land ID: " + irrigateLandEvent.getLand().getId() + " for Crop: " + irrigateLandEvent.getLand().getCrop()
                ));

                logger.error("Maximum retries exceeded when notifying sensor to irrigate: {}", landInfo.toString());

                irrigateLandEvent.getLand().setIrrigationStatus(Land.IrrigationStatus.IRRIGATION_FAILED);
                landRepository.save(irrigateLandEvent.getLand());
            }
        }
        catch(Exception e) {
            // Log error when saving to database
            logger.error("An exception occurred while trying to process automatic irrigation request for land with info: {}", landInfo.toString());
        }

    }
}
