package com.kifiya.automatic_irrigation_system.listeners;

import com.kifiya.automatic_irrigation_system.dao.Alerts;
import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.dto.ApiResponse;
import com.kifiya.automatic_irrigation_system.dto.LandInfo;
import com.kifiya.automatic_irrigation_system.events.IrrigateLandEvent;
import com.kifiya.automatic_irrigation_system.repositories.AlertsRepository;
import com.kifiya.automatic_irrigation_system.repositories.LandRepository;
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

    @Async
    @Override
    public void onApplicationEvent(IrrigateLandEvent irrigateLandEvent)
    {
        RestTemplate restTemplate = new RestTemplate();

        LandInfo landInfo = new LandInfo(irrigateLandEvent.getLand().getId(),
                irrigateLandEvent.getLand().getLength(), irrigateLandEvent.getLand().getWidth(),
                irrigateLandEvent.getLand().getArea(), irrigateLandEvent.getLand().getCrop(),
                irrigateLandEvent.getLand().getStatus(), irrigateLandEvent.getLand().getDateAdded(),
                irrigateLandEvent.getLand().getLastIrrigated(), irrigateLandEvent.getLand().getScheduledIrrigationTime());


        ResponseEntity<ApiResponse> responseEntity = null;
        int totalRetries = 0;

        while(totalRetries < maxRetries && responseEntity == null) {

            try {
                responseEntity = restTemplate.postForEntity(sensorURL, landInfo, ApiResponse.class);
                totalRetries += 1;
            }
            catch(Exception e) {
                // Catch the exception and allow another retry until we reach the required retries

            }
        }

        try {
            if(responseEntity == null && totalRetries >= maxRetries) {

                // Save this as we will need to issue an alert
                alertsRepository.save(new Alerts(
                        LocalDateTime.now(), "Exceeded max retries notifying sensor", null, "Max Retry Exceeded"
                ));

            }
            else if (responseEntity.getBody() != null && responseEntity.getStatusCode().value() == 201
                    && responseEntity.getBody().getResponseCode() != null
                    && responseEntity.getBody().getResponseCode().equals("0")) {

                irrigateLandEvent.getLand().setLastIrrigated(LocalDateTime.now());
                irrigateLandEvent.getLand().setStatus(Land.LandStatus.IRRIGATION_SUCCESSFUL);

                landRepository.save(irrigateLandEvent.getLand());
            } else {

                irrigateLandEvent.getLand().setLastIrrigated(LocalDateTime.now());
                irrigateLandEvent.getLand().setStatus(Land.LandStatus.IRRIGATION_FAILED);

                landRepository.save(irrigateLandEvent.getLand());
            }
        }
        catch(Exception e) {
            // Log error when saving to database
        }

    }
}
