package com.kifiya.automatic_irrigation_system.repositories;

import com.kifiya.automatic_irrigation_system.dao.Land;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface LandRepository extends CrudRepository<Land, Long> {

    List<Land> findByStatusOrderByAreaDesc(Land.LandStatus status);

    List<Land> findByScheduledIrrigationTime(LocalTime scheduledIrrigationTime);
    List<Land> findByScheduledIrrigationTimeGreaterThanEqualAndScheduledIrrigationTimeLessThanEqualAndIrrigationStatusAndStatus(
            LocalTime startScheduledIrrigationTime, LocalTime endScheduledIrrigationTime, Land.IrrigationStatus irrigationStatus,
            Land.LandStatus status);
}
