package com.acon.server.member.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RecentGuidedSpot {

    private final Long id;
    private final Long memberId;
    private final Long spotId;

    @Builder
    private RecentGuidedSpot(Long id, Long memberId, Long spotId) {
        this.id = id;
        this.memberId = memberId;
        this.spotId = spotId;
    }
}
