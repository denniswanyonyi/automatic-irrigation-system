package com.kifiya.automatic_irrigation_system.services;

import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.dto.AddLandApiRequest;
import com.kifiya.automatic_irrigation_system.dto.ApiResponse;
import com.kifiya.automatic_irrigation_system.dto.ListPlotsApiResponse;


public interface LandService {

//    Land configureLand(Long id);
//    Land editLand(Long id);
    ApiResponse saveLand(AddLandApiRequest apiRequest);

    ListPlotsApiResponse listAllPlots();
}
