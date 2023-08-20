package org.philbour.weatherservice.repository;

import org.philbour.weatherservice.model.dao.SensorReadingDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorReadingRepository extends JpaRepository<SensorReadingDao, Long> {

    // @Query("SELECT avg(sr.value) AS average FROM sensor_reading sr WHERE sr.metricType = :metricType AND
    // sr.timeOfReading BETWEEN :from AND :to")
    // int getAverageForMetric(@Param("metricType") String metricType, @Param("from") LocalDate from,
    // @Param("to") LocalDate to);

}
