package com.acon.server.member.infra.entity.member;

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
        name = "recent_guided_spot", uniqueConstraints = @UniqueConstraint(
                name = "unique_recent_guided_spot_member_id_spot_id", columnNames = {"member_id", "spot_id"}
        )
)
public class RecentGuidedSpotEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "spot_id", nullable = false)
    private Long spotId;

    @Builder
    public RecentGuidedSpotEntity(
            Long id,
            Long memberId,
            Long spotId
    ) {
        this.id = id;
        this.memberId = memberId;
        this.spotId = spotId;
    }
}
