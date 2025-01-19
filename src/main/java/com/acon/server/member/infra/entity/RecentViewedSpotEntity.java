package com.acon.server.member.infra.entity;

import com.acon.server.global.entity.BaseTimeEntity;
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
        name = "recent_viewed_spot",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_recent_viewed_spot_member_id_spot_id",
                columnNames = {"member_id", "spot_id"}
        )
)
public class RecentViewedSpotEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "spot_id", nullable = false)
    private Long spotId;

    @Builder
    public RecentViewedSpotEntity(
            Long id,
            Long memberId,
            Long spotId
    ) {
        this.id = id;
        this.memberId = memberId;
        this.spotId = spotId;
    }
}
