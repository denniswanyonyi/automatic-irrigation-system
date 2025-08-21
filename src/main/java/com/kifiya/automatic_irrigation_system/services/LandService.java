package com.kifiya.automatic_irrigation_system.services;

import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.dto.*;
import jakarta.validation.constraints.NotNull;


public interface LandService {

    ApiResponse configureLand(Long id, EditLandApiRequest apiRequest);
    ApiResponse editLand(Long id, EditLandApiRequest apiRequest);
    ApiResponse saveLand(AddLandApiRequest apiRequest);

    ListPlotsApiResponse listAllPlots(String requestRefID);
}
