package com.acon.server.spot.domain.entity;

import com.acon.server.spot.domain.enums.SpotType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Spot {

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
    private String adminDong;

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
            String adminDong
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
        this.adminDong = adminDong;
    }
}
