package com.acon.server.spot.domain.entity;

import com.acon.server.spot.domain.enums.SpotType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Getter
@ToString
public class Spot {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    private final Long id;
    private final String name;
    private final SpotType spotType;
    private final String address;

    private int localAcornCount;
    private LocalDateTime localAcornUpdatedAt;
    private int basicAcornCount;
    private LocalDateTime basicAcornUpdatedAt;
    private Double latitude;
    private Double longitude;
    private Point geom;
    private String legalDong;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Spot(
            Long id,
            String name,
            SpotType spotType,
            String address,
            Integer localAcornCount,
            LocalDateTime localAcornUpdatedAt,
            Integer basicAcornCount,
            LocalDateTime basicAcornUpdatedAt,
            Double latitude,
            Double longitude,
            Point geom,
            String legalDong,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.spotType = spotType;
        this.address = address;
        this.localAcornCount = localAcornCount;
        this.localAcornUpdatedAt = localAcornUpdatedAt;
        this.basicAcornCount = basicAcornCount;
        this.basicAcornUpdatedAt = basicAcornUpdatedAt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geom = geom;
        this.legalDong = legalDong;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void addAcorn(int acornCount, boolean isLocal) {
        if (isLocal) {
            addLocalAcorn(acornCount);
        } else {
            addBasicAcorn(acornCount);
        }
    }

    private void addLocalAcorn(int acornCount) {
        this.localAcornCount += acornCount;

        if (acornCount >= 4) { // TODO: 매직 넘버 yml로 관리
            this.localAcornUpdatedAt = LocalDateTime.now();
        }
    }

    private void addBasicAcorn(int acornCount) {
        this.basicAcornCount += acornCount;

        if (acornCount >= 4) {
            this.basicAcornUpdatedAt = LocalDateTime.now();
        }
    }

    public void updateCoordinate(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateGeom() {
        if (latitude != null && longitude != null) {
            this.geom = geometryFactory.createPoint(new Coordinate(longitude, latitude));
            this.geom.setSRID(4326);
        }
    }

    public void updateLegalDong(String legalDong) {
        if (latitude != null && longitude != null) {
            this.legalDong = legalDong;
        }
    }

    public void updateCreatedAt() {
        if (this.createdAt == null) {
            this.createdAt = this.updatedAt;
        }
    }
}
