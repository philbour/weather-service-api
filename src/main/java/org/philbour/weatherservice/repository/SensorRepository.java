package org.philbour.weatherservice.repository;

import org.philbour.weatherservice.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

}
