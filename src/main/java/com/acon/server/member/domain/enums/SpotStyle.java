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

    SENSE("분위기와 인테리어가 감각적인 곳"),
    NEW_FOOD("새로운 음식을 경험할 수 있는 곳"),
    REASONABLE("가격과 양이 합리적인 곳"),
    LUXURY("특별한 날을 위한 고급스러운 장소"),
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
