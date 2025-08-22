package com.kifiya.automatic_irrigation_system.services;

import com.kifiya.automatic_irrigation_system.dao.Alerts;
import com.kifiya.automatic_irrigation_system.dto.*;
import com.kifiya.automatic_irrigation_system.repositories.AlertsRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlertsServiceImpl implements AlertsService {

    @Autowired
    AlertsRepository alertsRepository;

    Logger logger = LoggerFactory.getLogger(AlertsServiceImpl.class);

    @Override
    public ListAlertsApiResponse getAlerts(String requestRefID)
    {
        logger.info("Started process to query alerts");

        ListAlertsApiResponse response = new ListAlertsApiResponse();

        response.setResponseParameters(new ArrayList<>());
        response.setAlertsInfo(new ArrayList<>());
        response.setRequestRefID(requestRefID == null ? UUID.randomUUID().toString() : requestRefID);


        try {
            List<Alerts> alerts = alertsRepository.findAllByIsReadOrderByIdDesc(false);

            logger.info("{} alerts retrieved", alerts.size());

            response.setAlertsInfo(new ArrayList<>());

            for(Alerts alert : alerts)
                response.getAlertsInfo().add(new AlertsInfo(alert.getId(),alert.getTitle(), alert.getDescription(),
                        alert.isRead(), alert.getDateAdded(), alert.getLastUpdated()));


            logger.info("Finished processing {} alerts", alerts.size());

            response.setResponseCode("0");
            response.setResponseDescription("Successfully retrieved alerts");
            response.getResponseParameters().add(new Parameter("DetailedMessage", "Successfully retrieved " + alerts.size() + " alerts. "));
        }
        catch(Exception e) {

            logger.error("An exception occurred while processing alerts, detailed message: {}", e.getMessage() , e);

            response.setResponseCode("0");
            response.setResponseDescription("An error occurred while querying alerts, please try again later");
        }

        return response;
    }

    @Override
    public ApiResponse updateAlert(Long id, @NotNull EditAlertApiRequest apiRequest)
    {
        logger.info("Received request to update alert, info: {}", apiRequest.toString());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setRequestRefID(apiRequest.getRequestRefID());

        try {

            Optional<Alerts> alert = alertsRepository.findById(id);

            if(alert.isEmpty()) {
                apiResponse.setResponseCode("404");
                apiResponse.setResponseDescription("Alert not found");

                return apiResponse;
            }

            alert.get().setRead(apiRequest.getAlertsInfo().isRead());
            alert.get().setLastUpdated(LocalDateTime.now());

            alertsRepository.save(alert.get());

            apiResponse.setResponseCode("0");
            apiResponse.setResponseDescription("Successfully updated alert");

        }
        catch(Exception e) {
            logger.error("An exception occurred while updating alert, detailed message: {}", e.getMessage() , e);

            apiResponse.setResponseCode("500");
            apiResponse.setResponseDescription("An error occurred while attempting to update alert.");
        }

        return apiResponse;
    }
}
