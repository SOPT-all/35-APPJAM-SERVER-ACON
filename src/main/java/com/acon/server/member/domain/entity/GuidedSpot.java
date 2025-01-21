package com.acon.server.member.domain.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GuidedSpot {

    private final Long id;
    private final Long memberId;
    private final Long spotId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public GuidedSpot(
            Long id,
            Long memberId,
            Long spotId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.memberId = memberId;
        this.spotId = spotId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
