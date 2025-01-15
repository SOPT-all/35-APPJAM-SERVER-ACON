package com.acon.server.review.infra.entity;

import com.acon.server.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class ReviewEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spot_id", nullable = false)
    private Long spotId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "acorn_count", nullable = false)
    private int acornCount;

    @Column(name = "local_acorn", nullable = false)
    private boolean localAcorn;

    @Builder
    public ReviewEntity(Long id, Long spotId, Long memberId, int acornCount, boolean localAcorn) {
        this.id = id;
        this.spotId = spotId;
        this.memberId = memberId;
        this.acornCount = acornCount;
        this.localAcorn = localAcorn;
    }
}
