package com.kifiya.automatic_irrigation_system.testdata;

import com.kifiya.automatic_irrigation_system.dao.CropMapping;
import com.kifiya.automatic_irrigation_system.repositories.CropMappingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CropMappingTestData {

    @Autowired
    CropMappingRepository cropMappingRepository;

    @PostConstruct
    public void init() {

        try {
            cropMappingRepository.save(new CropMapping(1000,"Teff"));
            cropMappingRepository.save(new CropMapping(450,"Coffee"));
            cropMappingRepository.save(new CropMapping(450,"Tea"));
            cropMappingRepository.save(new CropMapping(100,"Maize"));
            cropMappingRepository.save(new CropMapping(50,"Cassava"));
            cropMappingRepository.save(new CropMapping(500,"Barley"));
            cropMappingRepository.save(new CropMapping(750,"Wheat"));
            cropMappingRepository.save(new CropMapping(50,"Yam"));
            cropMappingRepository.save(new CropMapping(100,"Beans"));
            cropMappingRepository.save(new CropMapping(250,"Cocoa"));

            System.out.println("Finished initializing");
        }
        catch(Exception e) {
            // Catch exception during initialization
            System.out.println(e.getMessage());
        }
    }
}
