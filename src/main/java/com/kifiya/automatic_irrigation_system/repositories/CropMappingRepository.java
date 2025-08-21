package com.kifiya.automatic_irrigation_system.repositories;

import com.kifiya.automatic_irrigation_system.dao.Alerts;
import com.kifiya.automatic_irrigation_system.dao.CropMapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropMappingRepository extends CrudRepository<CropMapping, Long> {

}
