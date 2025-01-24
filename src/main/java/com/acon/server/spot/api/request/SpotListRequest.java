package com.acon.server.spot.api.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record SpotListRequest(
        @NotNull(message = "위도는 필수입니다.")
        @DecimalMin(value = "33.1", message = "위도는 최소 33.1°N 이상이어야 합니다.(대한민국 기준)")
        @DecimalMax(value = "38.6", message = "위도는 최대 38.6°N 이하이어야 합니다.(대한민국 기준)")
        Double latitude,
        @NotNull(message = "경도는 필수입니다.")
        @DecimalMin(value = "124.6", message = "경도는 최소 124.6°E 이상이어야 합니다.(대한민국 기준)")
        @DecimalMax(value = "131.9", message = "경도는 최대 131.9°E 이하이어야 합니다.(대한민국 기준)")
        Double longitude,
        @NotNull(message = "상세 조건은 필수입니다.")
        Condition condition
) {

    public record Condition(
            String spotType,
            List<Filter> filterList,
            @NotNull(message = "도보 가능 거리는 필수입니다.")
            @Positive(message = "도보 가능 거리는 양수여야 합니다.")
            Integer walkingTime,
            @Positive(message = "가격대는 양수여야 합니다.")
            Integer priceRange
    ) {

        public record Filter(
                @NotNull(message = "카테고리는 필수입니다.")
                String category,
                @NotNull(message = "옵션 리스트는 필수입니다.")
                List<String> optionList
        ) {

        }
    }
}
