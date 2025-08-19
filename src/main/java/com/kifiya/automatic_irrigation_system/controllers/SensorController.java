package com.kifiya.automatic_irrigation_system.controllers;


import com.kifiya.automatic_irrigation_system.dto.ApiResponse;
import com.kifiya.automatic_irrigation_system.dto.LandInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/sensor")
public class SensorController {
    /*
        This is a mock controller that will enable us test the Sensor functionality
    */

    @Value("${sensor_respond_with_failure}")
    private boolean sensorResponseWithFailure;

    public ResponseEntity<ApiResponse> receiveIrrigationRequest(@RequestBody LandInfo landInfo)
    {
        ApiResponse response = new ApiResponse();
        response.setRequestRefID(UUID.randomUUID().toString());

        if(sensorResponseWithFailure) {
            response.setResponseCode("500");
            response.setResponseDescription("Sensor unavailable at this moment");

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }

        response.setResponseCode("0");
        response.setResponseDescription("Sensor processed the request successfully");

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));
    }

}
