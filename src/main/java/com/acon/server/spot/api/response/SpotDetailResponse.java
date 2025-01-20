package com.acon.server.spot.api.response;

import com.acon.server.spot.domain.enums.SpotType;
import java.util.List;
import lombok.NonNull;

public record SpotDetailResponse(
        @NonNull
        Long id,
        @NonNull
        String name,
        @NonNull
        SpotType spotType,
        @NonNull
        List<String> imageList,
        @NonNull
        Boolean openStatus,
        @NonNull
        String address,
        @NonNull
        Integer localAcornCount,
        @NonNull
        Integer basicAcornCount,
        @NonNull
        Double latitude,
        @NonNull
        Double longitude
) {

}
