package com.acon.server.spot.infra.repository;

import com.acon.server.spot.infra.entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<OptionEntity, Long> {

}
