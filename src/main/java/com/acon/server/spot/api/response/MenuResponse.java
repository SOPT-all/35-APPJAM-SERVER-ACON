package com.acon.server.spot.api.response;

import lombok.Builder;

@Builder
public record MenuResponse(
        Long id,
        String name,
        int price,
        String image
) {

}
