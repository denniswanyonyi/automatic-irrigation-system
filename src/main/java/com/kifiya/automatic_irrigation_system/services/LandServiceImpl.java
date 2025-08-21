package com.kifiya.automatic_irrigation_system.services;

import com.kifiya.automatic_irrigation_system.dao.CropMapping;
import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.dto.*;
import com.kifiya.automatic_irrigation_system.events.AllocateTimeSlotEvent;
import com.kifiya.automatic_irrigation_system.repositories.CropMappingRepository;
import com.kifiya.automatic_irrigation_system.repositories.LandRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class LandServiceImpl implements LandService{

    @Value("${default_amount_of_water}")
    private int defaultAmountOfWater;

    @Autowired
    private LandRepository landRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private CropMappingRepository cropMappingRepository;

    private HashMap<String, Integer> cropMappingMap = new HashMap<>();

    Logger logger = LoggerFactory.getLogger(LandServiceImpl.class);

    public void initializeCropMapping() {

        logger.info("Started initializing crop mapping from database");

        Iterable<CropMapping> cropMappingIterable = cropMappingRepository.findAll();

        for(CropMapping mapping : cropMappingIterable)
            cropMappingMap.put(mapping.getCrop().toLowerCase(), mapping.getAmountOfWater());
    }

    @Override
    public ListPlotsApiResponse listAllPlots(String requestRefID)
    {
        logger.info("Started processing list all plots request");

        ListPlotsApiResponse apiResponse = new ListPlotsApiResponse();
        apiResponse.setRequestRefID(requestRefID == null ? UUID.randomUUID().toString() : requestRefID);


        try {
            Iterable<Land> plots = landRepository.findAll();

            apiResponse.setLandInfo(new ArrayList<>());

            for (Land land : plots) {
                apiResponse.getLandInfo().add(new LandInfo(land.getId(), land.getLength(), land.getWidth(), land.getArea(),
                        land.getCrop(), land.getAmountOfWater(), land.getStatus(), land.getDateAdded(), land.getLastIrrigated(), land.getScheduledIrrigationTime()));
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

    @Override
    public ApiResponse configureLand(@NotNull Long id, EditLandApiRequest apiRequest) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseParameters(new ArrayList<>());
        apiResponse.setRequestRefID(apiRequest.getRequestRefID());

        Optional<Land> land = landRepository.findById(id);

        if(land.isEmpty()) {
            apiResponse.setResponseDescription("Land not found");
            apiResponse.setResponseCode("404");

            return apiResponse;
        }

        // If irrigation time is updated
        if(apiRequest.getLandInfo().getScheduledIrrigationTime() != null && !land.get().getScheduledIrrigationTime().equals(apiRequest.getLandInfo().getScheduledIrrigationTime()))
            land.get().setScheduledIrrigationTime(apiRequest.getLandInfo().getScheduledIrrigationTime());

        // Update amount of water to irrigate & the amount is greater than 0
        if(land.get().getAmountOfWater() != apiRequest.getLandInfo().getAmountOfWater() && apiRequest.getLandInfo().getAmountOfWater() > 0)
            land.get().setAmountOfWater(apiRequest.getLandInfo().getAmountOfWater());

        try{

            landRepository.save(land.get());

            apiResponse.setResponseDescription("Successfully configured the land information");
            apiResponse.setResponseCode("0");
        }
        catch (Exception e) {
            apiResponse.setResponseDescription("An error occurred while configuring the land info");
            apiResponse.setResponseCode("500");

            apiResponse.getResponseParameters().add(new Parameter("DetailedMessage", e.getMessage()));
        }

        return apiResponse;
    }

    @Override
    public ApiResponse editLand(@NotNull Long id, @NotNull EditLandApiRequest apiRequest) {

        ApiResponse response = new ApiResponse();
        response.setRequestRefID(apiRequest.getRequestRefID());

        Optional<Land> land = landRepository.findById(id);

        if(land.isEmpty()) {
            response.setResponseDescription("Land not found");
            response.setResponseCode("404");
        }
        else {

            try {

                // IF crop changed
                if(!land.get().getCrop().equalsIgnoreCase(apiRequest.getLandInfo().getCrop()))
                    land.get().setCrop(apiRequest.getLandInfo().getCrop());

                // Update Status if it has changed
                if(land.get().getStatus() != apiRequest.getLandInfo().getStatus())
                    land.get().setStatus(apiRequest.getLandInfo().getStatus());

                if(land.get().getLength() != apiRequest.getLandInfo().getLength())
                    land.get().setLength(apiRequest.getLandInfo().getLength());

                if(land.get().getWidth() != apiRequest.getLandInfo().getWidth())
                    land.get().setWidth(apiRequest.getLandInfo().getWidth());

                landRepository.save(land.get());

                response.setResponseDescription("Successfully updated the land information");
                response.setResponseCode("0");
            }
            catch(Exception e) {
                response.setResponseDescription("An error occurred while updating the land information");
                response.setResponseCode("500");
            }
        }

        return response;
    }

    @Override
    public ApiResponse saveLand(AddLandApiRequest apiRequest)
    {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setRequestRefID(apiRequest.getRequestRefID());

        try {
            long area = apiRequest.getLandInfo().getLength() * apiRequest.getLandInfo().getWidth();

            initializeCropMapping();

            int amountOfWater = defaultAmountOfWater;

            // Overwrite the amount of water used for irrigation of the crop if found in the mapping, otherwise
            // use default water as value

            if(cropMappingMap.containsKey(apiRequest.getLandInfo().getCrop().toLowerCase())) {
                amountOfWater = cropMappingMap.get(apiRequest.getLandInfo().getCrop().toLowerCase());
            }

            Land land = new Land(
                    apiRequest.getLandInfo().getLength(), apiRequest.getLandInfo().getWidth(), area, apiRequest.getLandInfo().getCrop(),
                    amountOfWater, Land.LandStatus.ACTIVE, LocalDateTime.now(), null
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
