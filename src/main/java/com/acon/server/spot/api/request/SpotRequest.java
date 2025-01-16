package com.acon.server.spot.api.request;

import jakarta.validation.constraints.NotNull;

public record SpotRequest(
        @NotNull
        Long spotId
) {

}
