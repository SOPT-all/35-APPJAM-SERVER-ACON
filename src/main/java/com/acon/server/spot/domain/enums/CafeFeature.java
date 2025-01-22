package com.acon.server.spot.domain.enums;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CafeFeature {
    LARGE,
    GOOD_VIEW,
    DESSERT,
    TERRACE,
    EXCLUDE_FRANCHISE;

    private static final Map<String, CafeFeature> CAFE_FEATURE_MAP = new HashMap<>();

    static {
        for (CafeFeature cafeFeature : CafeFeature.values()) {
            CAFE_FEATURE_MAP.put(cafeFeature.name(), cafeFeature);
        }
    }

    public static CafeFeature fromValue(String value) {
        CafeFeature spotType = CAFE_FEATURE_MAP.get(value);

        if (spotType == null) {
            throw new BusinessException(ErrorType.INVALID_SPOT_TYPE_ERROR);
        }

        return spotType;
    }
}
