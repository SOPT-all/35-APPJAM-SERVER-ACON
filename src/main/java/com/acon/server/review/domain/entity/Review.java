package com.acon.server.review.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Review {

    private final Long id;
    private final Long spotId;
    private final Long memberId;
    private final int acornCount;
    private final boolean localAcorn;

    @Builder
    public Review(
            Long id,
            Long spotId,
            Long memberId,
            int acornCount,
            boolean localAcorn
    ) {
        this.id = id;
        this.spotId = spotId;
        this.memberId = memberId;
        this.acornCount = acornCount;
        this.localAcorn = localAcorn;
    }
}
