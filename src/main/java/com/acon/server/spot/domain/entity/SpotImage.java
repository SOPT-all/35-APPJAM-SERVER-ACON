package com.acon.server.spot.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SpotImage {

    private final Long id;
    private final Long spotId;
    private final String image;

    @Builder
    public SpotImage(
            Long id,
            Long spotId,
            String image
    ) {
        this.id = id;
        this.spotId = spotId;
        this.image = image;
    }
}
