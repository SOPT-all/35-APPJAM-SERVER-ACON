package com.acon.server.spot.infra.entity;

import com.acon.server.global.entity.BaseTimeEntity;
import com.acon.server.spot.domain.enums.SpotType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "spot")
public class SpotEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "spot_type", nullable = false)
    private SpotType spotType;

    @Column(name = "local_acorn_count", nullable = false)
    private int localAcornCount;

    @Column(name = "local_acorn_updated_at")
    private LocalDateTime localAcornUpdatedAt;

    @Column(name = "basic_acorn_count", nullable = false)
    private int basicAcornCount;

    @Column(name = "basic_acorn_updated_at")
    private LocalDateTime basicAcornUpdatedAt;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "admin_dong")
    private String adminDong;

    @Builder
    public SpotEntity(
            Long id,
            String name,
            SpotType spotType,
            Integer localAcornCount,
            LocalDateTime localAcornUpdatedAt,
            Integer basicAcornCount,
            LocalDateTime basicAcornUpdatedAt,
            String address,
            Double latitude,
            Double longitude,
            String adminDong
    ) {
        this.id = id;
        this.name = name;
        this.spotType = spotType;
        // TODO: 영속성 엔티티에서 기본값을 설정하는 로직을 도메인 엔티티로 이동
        this.localAcornCount = localAcornCount != null ? localAcornCount : 0;
        this.localAcornUpdatedAt = localAcornUpdatedAt;
        this.basicAcornCount = basicAcornCount != null ? basicAcornCount : 0;
        this.basicAcornUpdatedAt = basicAcornUpdatedAt;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adminDong = adminDong;
    }
}
