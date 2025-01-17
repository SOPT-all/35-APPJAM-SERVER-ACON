package com.acon.server.spot.infra.repository;

import com.acon.server.spot.infra.entity.SpotEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<SpotEntity, Long> {

    List<SpotEntity> findTop10ByNameContainsIgnoreCase(String keyword);
}
