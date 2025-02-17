package com.acon.server.spot.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "spot_option",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_spot_option_spot_id_option_id",
                columnNames = {"spot_id", "option_id"}
        )
)
public class SpotOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spot_id", nullable = false)
    private Long spotId;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Builder
    public SpotOptionEntity(
            Long id,
            Long spotId,
            Long optionId
    ) {
        this.id = id;
        this.spotId = spotId;
        this.optionId = optionId;
    }
}
