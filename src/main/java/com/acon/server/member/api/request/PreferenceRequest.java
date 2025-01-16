package com.acon.server.member.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PreferenceRequest(
        @NotEmpty(message = "dislikeFoodList가 빈 값입니다.")
        List<String> dislikeFoodList,
        @NotEmpty(message = "favoriteCuisineRank가 빈 값입니다.")
        List<String> favoriteCuisineRank,
        @NotBlank(message = "favoriteSpotType가 빈 값입니다.")
        String favoriteSpotType,
        @NotBlank(message = "favoriteSpotStyle가 빈 값입니다.")
        String favoriteSpotStyle,
        @NotEmpty(message = "favoriteSpotRank가 빈 값입니다.")
        List<String> favoriteSpotRank
) {

}
