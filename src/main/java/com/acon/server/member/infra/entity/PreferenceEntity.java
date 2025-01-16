package com.acon.server.member.infra.entity;

import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.spot.domain.enums.SpotType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "preference")
public class PreferenceEntity {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dislike_food_list", nullable = false)
    private List<DislikeFood> dislikeFoodList;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "favorite_cuisine_rank", nullable = false)
    private List<Cuisine> favoriteCuisineRank;

    @Enumerated(EnumType.STRING)
    @Column(name = "favorite_spot_type", nullable = false)
    private SpotType favoriteSpotType;

    @Enumerated(EnumType.STRING)
    @Column(name = "favorite_spot_style", nullable = false)
    private SpotStyle favoriteSpotStyle;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "favorite_spot_rank", nullable = false)
    private List<FavoriteSpot> favoriteSpotRank;

    @Builder
    public PreferenceEntity(
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
