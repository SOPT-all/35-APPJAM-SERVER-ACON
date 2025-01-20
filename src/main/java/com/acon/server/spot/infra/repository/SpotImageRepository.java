package com.acon.server.spot.infra.repository;

import com.acon.server.spot.infra.entity.SpotImageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotImageRepository extends JpaRepository<SpotImageEntity, Long> {

    List<SpotImageEntity> findAllBySpotId(Long spotId);
}
