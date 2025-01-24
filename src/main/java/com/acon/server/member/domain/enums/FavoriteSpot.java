package com.acon.server.member.domain.enums;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FavoriteSpot {

    SENSE,
    NEW_FOOD,
    REASONABLE,
    LUXURY,
    ;

    private static final Map<String, FavoriteSpot> FAVORITE_SPOT_MAP = new HashMap<>();

    static {
        for (FavoriteSpot favoriteSpot : FavoriteSpot.values()) {
            FAVORITE_SPOT_MAP.put(favoriteSpot.name(), favoriteSpot);
        }
    }

    public static FavoriteSpot fromValue(String value) {
        FavoriteSpot favoriteSpot = FAVORITE_SPOT_MAP.get(value);

        if (favoriteSpot == null) {
            throw new BusinessException(ErrorType.INVALID_FAVORITE_SPOT_ERROR);
        }

        return favoriteSpot;
    }

    public static FavoriteSpot matchFavoriteSpot(String name) {
        try {
            return FavoriteSpot.fromValue(name);
        } catch (Exception e) {
            return null;
        }
    }
}
