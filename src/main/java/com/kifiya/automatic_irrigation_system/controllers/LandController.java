package com.kifiya.automatic_irrigation_system.controllers;

import com.kifiya.automatic_irrigation_system.dto.AddLandApiRequest;
import com.kifiya.automatic_irrigation_system.dto.ApiResponse;
import com.kifiya.automatic_irrigation_system.dto.ListPlotsApiResponse;
import com.kifiya.automatic_irrigation_system.services.LandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/land")
public class LandController {

    @Autowired
    LandService landService;

    @GetMapping
    public ListPlotsApiResponse ListPlots()
    {
        return landService.listAllPlots();
    }

    @PostMapping
    public ApiResponse addLand(@RequestBody AddLandApiRequest apiRequest)
    {
        return landService.saveLand(apiRequest);
    }

    @PutMapping("/configure")
    public void configureLand()
    {

    }

    @PutMapping("")
    public void editLand()
    {

    }
}
