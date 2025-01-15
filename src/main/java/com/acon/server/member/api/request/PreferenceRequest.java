package com.acon.server.member.api.request;

import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.member.domain.enums.SpotType;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PreferenceRequest(
        @NotNull
        List<DislikeFood> dislikeFoodList,
        @NotNull
        List<Cuisine> favoriteCuisineRank,
        SpotType favoriteSpotType,
        SpotStyle favoriteSpotStyle,
        @NotNull
        List<FavoriteSpot> favoriteSpotRank
) {

}
