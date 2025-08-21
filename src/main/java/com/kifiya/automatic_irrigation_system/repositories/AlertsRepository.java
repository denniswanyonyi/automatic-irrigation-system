package com.kifiya.automatic_irrigation_system.repositories;

import com.kifiya.automatic_irrigation_system.dao.Alerts;
import com.kifiya.automatic_irrigation_system.dao.Land;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface AlertsRepository extends CrudRepository<Alerts, Long> {

    List<Alerts> findAllByOrderByIdDesc();
}
