package com.acon.server.spot.api.response;

import java.util.List;

public record SpotListResponse(
        List<RecommendedSpot> spotList
) {

    public static record RecommendedSpot(
            long id,                        // 장소 ID
            String image,                   // 장소 이미지 URL
            Integer matchingRate,           // 취향 일치율 (Optional)
            String type,                    // 장소 분류
            String name,                    // 장소 이름
            int walkingTime                 // 도보 시간
    ) {

    }
}
