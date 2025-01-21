package com.acon.server.spot.infra.repository;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.spot.infra.entity.SpotEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotRepository extends JpaRepository<SpotEntity, Long> {

    List<SpotEntity> findTop10ByNameContainsIgnoreCase(String keyword);

    List<SpotEntity> findAllByLatitudeIsNullOrLongitudeIsNull();

    default SpotEntity findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(
                () -> new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR)
        );
    }

    @Query(value = """
            SELECT s.id, s.name
            FROM spot s
            WHERE ST_DWithin(
                s.geom,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326),
                :radius
            )
            ORDER BY ST_DistanceSphere(
                s.geom,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)
            )
            LIMIT :limit
            """, nativeQuery = true)
    List<Object[]> findNearestSpots(
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius") double radius,
            @Param("limit") int limit
    );

    @Query(value = """
            SELECT ST_DistanceSphere(
                ST_SetSRID(ST_MakePoint(:lon1, :lat1), 4326),
                ST_SetSRID(ST_MakePoint(:lon2, :lat2), 4326)
            )
            """, nativeQuery = true)
    Double calculateDistance(
            @Param("lon1") Double lon1,
            @Param("lat1") Double lat1,
            @Param("lon2") Double lon2,
            @Param("lat2") Double lat2
    );
}
