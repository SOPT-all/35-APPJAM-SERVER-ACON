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
public enum DislikeFood {

    DAKBAL("닭발"),
    HOE_YUKHOE("회/육회"),
    GOPCHANG_MAKCHANG_DAECHANG("곱창/막창/대창"),
    SUNDAE_SEONJI("순대/선지"),
    YANGGOGI("양고기"),
    ;

    private final String value;

    private static final Map<String, DislikeFood> DISLIKE_FOOD_MAP = new HashMap<>();

    static {
        for (DislikeFood dislikeFood : DislikeFood.values()) {
            DISLIKE_FOOD_MAP.put(dislikeFood.getValue(), dislikeFood);
        }
    }

    public static DislikeFood fromValue(String value) {
        DislikeFood dislikeFood = DISLIKE_FOOD_MAP.get(value);

        if (dislikeFood == null) {
            throw new BusinessException(ErrorType.INVALID_DISLIKE_FOOD_ERROR);
        }

        return dislikeFood;
    }
}
