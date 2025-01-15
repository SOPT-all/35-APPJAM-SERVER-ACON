package com.acon.server.member.domain.entity;

import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.spot.domain.enums.SpotType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Preference {

    private final Long memberId;

    private List<DislikeFood> dislikeFoodList;
    private List<Cuisine> favoriteCuisineRank;
    private SpotType favoriteSpotType;
    private SpotStyle favoriteSpotStyle;
    private List<FavoriteSpot> favoriteSpotRank;

    @Builder
    private Preference(
            Long memberId,
            List<DislikeFood> dislikeFoodList,
            List<Cuisine> favoriteCuisineRank,
            SpotType favoriteSpotType,
            SpotStyle favoriteSpotStyle,
            List<FavoriteSpot> favoriteSpotRank
    ) {
        this.memberId = memberId;
        this.dislikeFoodList = dislikeFoodList;
        this.favoriteCuisineRank = favoriteCuisineRank;
        this.favoriteSpotType = favoriteSpotType;
        this.favoriteSpotStyle = favoriteSpotStyle;
        this.favoriteSpotRank = favoriteSpotRank;
    }
}
