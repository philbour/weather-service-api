package org.philbour.weatherservice.repository;

import org.philbour.weatherservice.model.dao.MetricDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRepository extends JpaRepository<MetricDao, Long> {

}
