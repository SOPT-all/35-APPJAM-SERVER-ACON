package com.acon.server.spot.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.NonNull;

@Builder
@JsonInclude(Include.NON_NULL)
public record MenuResponse(
        @NonNull
        Long id,
        @NonNull
        String name,
        @NonNull
        Integer price,
        String image
) {

}
