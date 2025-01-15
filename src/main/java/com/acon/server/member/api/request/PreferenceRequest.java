package com.acon.server.member.api.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PreferenceRequest(
        @NotNull
        List<String> dislikeFoodList,
        @NotNull
        List<String> favoriteCuisineRank,
        @NotNull
        String favoriteSpotType,
        @NotNull
        String favoriteSpotStyle,
        @NotNull
        List<String> favoriteSpotRank
) {

}
