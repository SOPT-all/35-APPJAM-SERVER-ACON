package com.acon.server.spot.api.response;

import java.util.List;

public record SearchSpotListResponse(
        List<SearchSpotResponse> spotList
) {

}
