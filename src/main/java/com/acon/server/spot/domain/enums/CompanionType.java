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
public enum CompanionType {
    FAMILY,
    DATE,
    FRIEND,
    ALONE,
    GROUP;

    private static final Map<String, CompanionType> COMPANION_TYPE_MAP = new HashMap<>();

    static {
        for (CompanionType companionType : CompanionType.values()) {
            COMPANION_TYPE_MAP.put(companionType.name(), companionType);
        }
    }

    public static CompanionType fromValue(String value) {
        CompanionType type = COMPANION_TYPE_MAP.get(value);

        if (type == null) {
            throw new BusinessException(ErrorType.INVALID_SPOT_TYPE_ERROR);
        }

        return type;
    }
}
