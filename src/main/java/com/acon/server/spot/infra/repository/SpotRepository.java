package com.acon.server.spot.infra.repository;

import com.acon.server.spot.infra.entity.SpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<SpotEntity, Long> {

}
