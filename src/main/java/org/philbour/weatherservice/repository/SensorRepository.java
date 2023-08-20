package org.philbour.weatherservice.repository;

import org.philbour.weatherservice.model.dao.SensorDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<SensorDao, Long> {

}
