package com.kifiya.automatic_irrigation_system.services;


import com.kifiya.automatic_irrigation_system.dto.ApiResponse;
import com.kifiya.automatic_irrigation_system.dto.EditAlertApiRequest;
import com.kifiya.automatic_irrigation_system.dto.ListAlertsApiResponse;
import jakarta.validation.constraints.NotNull;

public interface AlertsService {

    ListAlertsApiResponse getAlerts(String requestRefID);
    ApiResponse updateAlert(Long id, EditAlertApiRequest apiRequest);

}
