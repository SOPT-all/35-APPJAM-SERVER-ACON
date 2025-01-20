package com.acon.server.spot.infra.repository;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.spot.infra.entity.SpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotRepository extends JpaRepository<SpotEntity, Long> {

    default SpotEntity findByIdOrElseThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR));
    }

    // TODO: 함수 위치에 대한 고민 필요
    @Query(value = "SELECT ST_DistanceSphere(" +
            "ST_SetSRID(ST_MakePoint(:lon1, :lat1), 4326), " +
            "ST_SetSRID(ST_MakePoint(:lon2, :lat2), 4326))",
            nativeQuery = true)
    Double calculateDistance(@Param("lon1") Double lon1,
                             @Param("lat1") Double lat1,
                             @Param("lon2") Double lon2,
                             @Param("lat2") Double lat2);
}
