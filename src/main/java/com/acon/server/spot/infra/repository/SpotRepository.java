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

    List<SpotEntity> findTop10ByNameStartingWith(String prefix);

    @Query(value = "SELECT * FROM spot WHERE name LIKE %:keyword% LIMIT :limit", nativeQuery = true)
    List<SpotEntity> findByNameContainingWithLimit(@Param("keyword") String keyword, @Param("limit") int limit);

    List<SpotEntity> findAllByLatitudeIsNullOrLongitudeIsNullOrGeomIsNullOrLegalDongIsNull();

    default SpotEntity findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(
                () -> new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR)
        );
    }

    @Query(value = "SELECT * FROM spot ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<SpotEntity> findRandomSpots(int limit);

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
    List<Object[]> findNearestSpotList(
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius") double radius,
            @Param("limit") int limit
    );

    @Query(value = """
            SELECT ST_DistanceSphere(
                s.geom,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)
            )
            FROM spot s
            WHERE s.id = :spotId
            """, nativeQuery = true)
    Double calculateDistanceFromSpot(
            @Param("spotId") Long spotId,
            @Param("longitude") Double longitude,
            @Param("latitude") Double latitude
    );
}
