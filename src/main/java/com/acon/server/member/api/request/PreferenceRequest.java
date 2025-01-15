package com.acon.server.member.api.request;

import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.member.domain.enums.SpotType;
import java.util.List;

public record PreferenceRequest(
        List<DislikeFood> dislikeFoodList,
        List<Cuisine> favoriteCuisineRank,
        SpotType favoriteSpotType,
        SpotStyle favoriteSpotStyle,
        List<FavoriteSpot> favoriteSpotRank
) {

}
