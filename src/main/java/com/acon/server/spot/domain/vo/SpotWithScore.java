package com.acon.server.spot.domain.vo;

import com.acon.server.spot.infra.entity.SpotEntity;

public record SpotWithScore(
        SpotEntity spotEntity,
        double score,
        int matchingRate
) {

}
