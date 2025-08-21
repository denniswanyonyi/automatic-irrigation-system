package com.kifiya.automatic_irrigation_system.controllers;


import com.kifiya.automatic_irrigation_system.dto.ApiResponse;
import com.kifiya.automatic_irrigation_system.dto.EditAlertApiRequest;
import com.kifiya.automatic_irrigation_system.dto.ListAlertsApiResponse;
import com.kifiya.automatic_irrigation_system.services.AlertsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alerts")
public class AlertsController {

    @Autowired
    AlertsService alertsService;

    @GetMapping
    public ListAlertsApiResponse getAlerts(@RequestHeader(required = false, name="RequestRefID") String requestRefID)
    {
        return alertsService.getAlerts(requestRefID);
    }

    @PutMapping("/{id}")
    public ApiResponse updateAlerts(@PathVariable("id") Long id, @RequestBody EditAlertApiRequest apiRequest) {

        return alertsService.updateAlert(id, apiRequest);
    }
}
