package com.acon.server.spot.api.response;

import com.acon.server.spot.domain.enums.SpotType;
import lombok.Builder;

@Builder
public record SearchSpotResponse(
        Long spotId,
        String name,
        String address,
        SpotType spotType
) {

}
