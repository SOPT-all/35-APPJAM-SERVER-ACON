package com.acon.server.spot.infra.repository;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.spot.infra.entity.SpotEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<SpotEntity, Long> {

    List<SpotEntity> findTop10ByNameContainsIgnoreCase(String keyword);
  
    default SpotEntity findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(
                () -> new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR)
        );
    }

    List<SpotEntity> findAllByLatitudeIsNullOrLongitudeIsNull();
}
