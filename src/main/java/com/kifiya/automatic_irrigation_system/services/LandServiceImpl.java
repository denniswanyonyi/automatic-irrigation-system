package com.kifiya.automatic_irrigation_system.services;

import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.dto.AddLandApiRequest;
import com.kifiya.automatic_irrigation_system.dto.ApiResponse;
import com.kifiya.automatic_irrigation_system.dto.LandInfo;
import com.kifiya.automatic_irrigation_system.dto.ListPlotsApiResponse;
import com.kifiya.automatic_irrigation_system.events.AllocateTimeSlotEvent;
import com.kifiya.automatic_irrigation_system.repositories.LandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class LandServiceImpl implements LandService{

    @Autowired
    private LandRepository landRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public ListPlotsApiResponse listAllPlots()
    {
        ListPlotsApiResponse apiResponse = new ListPlotsApiResponse();

        try {
            Iterable<Land> plots = landRepository.findAll();

            apiResponse.setLandInfo(new ArrayList<>());

            for (Land land : plots) {
                apiResponse.getLandInfo().add(new LandInfo(land.getId(), land.getLength(), land.getWidth(), land.getArea(),
                        land.getCrop(), land.getStatus(), land.getDateAdded(), land.getLastIrrigated(), land.getScheduledIrrigationTime()));
            }

            apiResponse.setResponseCode("0");
            apiResponse.setResponseDescription("Successfully retrieved details of all plots of land.");
        }
        catch(Exception e) {

            apiResponse.setResponseCode("500");
            apiResponse.setResponseDescription("An exception occurred while retrieving the plots of land");

        }

        return apiResponse;
    }

    public ApiResponse saveLand(AddLandApiRequest apiRequest)
    {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setRequestRefID(apiRequest.getRequestRefID());

        try {
            long area = apiRequest.getLandInfo().getLength() * apiRequest.getLandInfo().getWidth();

            Land land = new Land(
                    apiRequest.getLandInfo().getLength(), apiRequest.getLandInfo().getWidth(), area, apiRequest.getLandInfo().getCrop(),
                    Land.LandStatus.ACTIVE, LocalDateTime.now(), null
            );

            landRepository.save(land);

            eventPublisher.publishEvent(new AllocateTimeSlotEvent(this, land));

            apiResponse.setResponseCode("0");
            apiResponse.setResponseDescription("Successfully added new land");
        }
        catch(Exception e) {
            apiResponse.setResponseCode("0");
            apiResponse.setResponseDescription("An error occurred while attempting to add new land, please try again later.");
        }

        return apiResponse;
    }
}
