package com.acon.server.member.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RecentViewedSpot {

    private final Long id;
    private final Long memberId;
    private final Long spotId;

    @Builder
    private RecentViewedSpot(Long id, Long memberId, Long spotId) {
        this.id = id;
        this.memberId = memberId;
        this.spotId = spotId;
    }
}
