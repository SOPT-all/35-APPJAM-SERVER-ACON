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
public enum Cuisine {

    KOREAN,
    WESTERN,
    CHINESE,
    JAPANESE,
    KOREAN_STREET,
    ASIAN,
    ;

    private static final Map<String, Cuisine> CUISINE_MAP = new HashMap<>();

    static {
        for (Cuisine cuisine : Cuisine.values()) {
            CUISINE_MAP.put(cuisine.name(), cuisine);
        }
    }

    public static Cuisine fromValue(String value) {
        Cuisine cuisine = CUISINE_MAP.get(value);

        if (cuisine == null) {
            throw new BusinessException(ErrorType.INVALID_CUISINE_ERROR);
        }

        return cuisine;
    }
}
