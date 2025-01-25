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

    VINTAGE,
    MODERN,
    TRADITIONAL, // TODO: 이후 삭제
    ;

    private static final Map<String, SpotStyle> SPOT_STYLE_MAP = new HashMap<>();

    static {
        for (SpotStyle spotStyle : SpotStyle.values()) {
            SPOT_STYLE_MAP.put(spotStyle.name(), spotStyle);
        }
    }

    public static SpotStyle fromValue(String value) {
        SpotStyle spotStyle = SPOT_STYLE_MAP.get(value);

        if (spotStyle == null) {
            throw new BusinessException(ErrorType.INVALID_SPOT_STYLE_ERROR);
        }

        return spotStyle;
    }

    public static SpotStyle matchSpotStyle(String name) {
        try {
            return SpotStyle.fromValue(name);
        } catch (Exception e) {
            return null;
        }
    }
}
