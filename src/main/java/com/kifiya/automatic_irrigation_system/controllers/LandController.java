package com.kifiya.automatic_irrigation_system.controllers;

import com.kifiya.automatic_irrigation_system.dto.*;
import com.kifiya.automatic_irrigation_system.services.LandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/land")
public class LandController {

    @Autowired
    LandService landService;

    @GetMapping
    public ListPlotsApiResponse ListPlots(@RequestHeader(required = false, name="RequestRefID") String requestRefID)
    {
        return landService.listAllPlots(requestRefID);
    }

    @PostMapping
    public ApiResponse addLand(@RequestBody AddLandApiRequest apiRequest)
    {
        return landService.saveLand(apiRequest);
    }

    @PutMapping("/configure/{id}")
    public ApiResponse configureLand(@PathVariable("id") Long id, @RequestBody EditLandApiRequest apiRequest)
    {
        return landService.configureLand(id, apiRequest);
    }

    @PutMapping("/{id}")
    public ApiResponse editLand(@RequestBody EditLandApiRequest apiRequest, @PathVariable("id") Long id)
    {
        return landService.editLand(id, apiRequest);
    }
}
