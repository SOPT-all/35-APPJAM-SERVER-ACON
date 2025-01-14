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
public enum SpotType {

    RESTAURANT("RESTAURANT"),
    CAFE("CAFE"),
    ;

    private final String value;

    private static final Map<String, SpotType> SPOT_TYPE_MAP = new HashMap<>();

    static {
        for (SpotType spotType : SpotType.values()) {
            SPOT_TYPE_MAP.put(spotType.getValue(), spotType);
        }
    }

    public static SpotType fromValue(String value) {
        SpotType spotType = SPOT_TYPE_MAP.get(value);

        if (spotType == null) {
            throw new BusinessException(ErrorType.INVALID_SPOT_TYPE_ERROR);
        }

        return spotType;
    }
}
