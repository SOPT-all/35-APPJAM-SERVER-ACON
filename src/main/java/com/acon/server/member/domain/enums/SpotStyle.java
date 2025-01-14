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
public enum SpotStyle {

    SENSE("SENSE"),
    NEW_FOOD("새NEW_FOOD"),
    REASONABLE("REASONABLE"),
    LUXURY("LUXURY"),
    ;

    private final String value;

    private static final Map<String, SpotStyle> SPOT_STYLE_MAP = new HashMap<>();

    static {
        for (SpotStyle spotStyle : SpotStyle.values()) {
            SPOT_STYLE_MAP.put(spotStyle.getValue(), spotStyle);
        }
    }

    public static SpotStyle fromValue(String value) {
        SpotStyle spotStyle = SPOT_STYLE_MAP.get(value);

        if (spotStyle == null) {
            throw new BusinessException(ErrorType.INVALID_SPOT_STYLE_ERROR);
        }

        return spotStyle;
    }
}
