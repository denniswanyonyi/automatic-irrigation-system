package com.kifiya.automatic_irrigation_system.testdata;


import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.repositories.LandRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class PlotsTestData {

    @Autowired
    LandRepository landRepository;

    @PostConstruct
    public void init() {

        try {
            // ACTIVE plots - that will be irrigated
            landRepository.save(new Land(2500, 3100, "Coffee", 750, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(1)));
            landRepository.save(new Land(15500, 25500, "Tea", 600, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(1)));
            landRepository.save(new Land(100, 200, "Maize", 183, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(2)));
            landRepository.save(new Land(50, 70, "Beans", 299, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(3)));
            landRepository.save(new Land(2000, 2500, "Barley", 23492, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(4)));
            landRepository.save(new Land(175, 200, "Cassava", 100, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(5)));
            landRepository.save(new Land(100, 50, "Potatoes", 250, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(6)));
            landRepository.save(new Land(5000, 2000, "Cocoa", 900, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(7)));
            landRepository.save(new Land(12500, 25000, "Wheat", 120, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(8)));
            landRepository.save(new Land(1100, 1200, "Bananas", 500, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(9)));
            landRepository.save(new Land(1000, 2000, "Coffee", 750, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(10)));
            landRepository.save(new Land(2500, 3100, "Coffee", 750, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(10)));
            landRepository.save(new Land(15500, 25500, "Tea", 600, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(11)));
            landRepository.save(new Land(8800, 5700, "Tea", 600, Land.LandStatus.ACTIVE, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(11)));

            // SUSPENDED plots - that irrigation will be skipped
            landRepository.save(new Land(1200, 2500, "Maize", 100, Land.LandStatus.SUSPENDED, Land.IrrigationStatus.PENDING_IRRIGATION, LocalDateTime.now(), LocalTime.now().plusMinutes(2)));
            landRepository.save(new Land(1500, 700, "Beans", 140, Land.LandStatus.SUSPENDED, Land.IrrigationStatus.PENDING_IRRIGATION,LocalDateTime.now(), LocalTime.now().plusMinutes(3)));
            landRepository.save(new Land(2500, 3500, "Barley", 3000, Land.LandStatus.SUSPENDED, Land.IrrigationStatus.PENDING_IRRIGATION,LocalDateTime.now(), LocalTime.now().plusMinutes(4)));
            landRepository.save(new Land(2200, 2000, "Cassava", 120, Land.LandStatus.SUSPENDED, Land.IrrigationStatus.PENDING_IRRIGATION,LocalDateTime.now(), LocalTime.now().plusMinutes(5)));
            landRepository.save(new Land(750, 500, "Potatoes", 300, Land.LandStatus.SUSPENDED, Land.IrrigationStatus.PENDING_IRRIGATION,LocalDateTime.now(), LocalTime.now().plusMinutes(6)));
            landRepository.save(new Land(15750, 12500, "Cocoa", 850, Land.LandStatus.SUSPENDED, Land.IrrigationStatus.PENDING_IRRIGATION,LocalDateTime.now(), LocalTime.now().plusMinutes(7)));
            landRepository.save(new Land(21500, 25500, "Wheat", 900, Land.LandStatus.SUSPENDED, Land.IrrigationStatus.PENDING_IRRIGATION,LocalDateTime.now(), LocalTime.now().plusMinutes(8)));
            landRepository.save(new Land(400, 250, "Bananas", 150, Land.LandStatus.SUSPENDED, Land.IrrigationStatus.PENDING_IRRIGATION,LocalDateTime.now(), LocalTime.now().plusMinutes(9)));
            landRepository.save(new Land(45000, 28000, "Coffee", 1750, Land.LandStatus.SUSPENDED, Land.IrrigationStatus.PENDING_IRRIGATION,LocalDateTime.now(), LocalTime.now().plusMinutes(10)));
            landRepository.save(new Land(26500, 37250, "Tea", 800, Land.LandStatus.SUSPENDED, Land.IrrigationStatus.PENDING_IRRIGATION,LocalDateTime.now(), LocalTime.now().plusMinutes(11)));
        }
        catch(Exception e) {
            // log any failures during initialization

        }
    }
}
