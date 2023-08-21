package org.philbour.weatherservice.repository;

import org.philbour.weatherservice.model.dao.SensorReadingDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface SensorReadingRepository extends JpaRepository<SensorReadingDao, Long> {

    @Query("SELECT avg(mv.metricValue) AS average FROM SensorReadingDao sr JOIN sr.sensor s JOIN sr.metrics mv JOIN mv.metric m"
            + " WHERE s.id = :sensorId AND m.metricType = :metricType AND sr.timeOfReading BETWEEN :from AND :to")
    int getAverageForMetric(@Param("sensorId") Long sensorId, @Param("metricType") String metricType,
            @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT avg(mv.metricValue) AS average FROM SensorReadingDao sr JOIN sr.metrics mv JOIN mv.metric m"
            + " WHERE m.metricType = :metricType AND sr.timeOfReading BETWEEN :from AND :to")
    int getAverageForMetric(@Param("metricType") String metricType, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

}
