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
public enum SocialType {

    GOOGLE("GOOGLE"),
    APPLE("APPLE"),
    ;

    private final String value;

    private static final Map<String, SocialType> SOCIAL_TYPE_MAP = new HashMap<>();

    static {
        for (SocialType socialType : SocialType.values()) {
            SOCIAL_TYPE_MAP.put(socialType.getValue(), socialType);
        }
    }

    public static SocialType fromValue(String value) {
        SocialType socialType = SOCIAL_TYPE_MAP.get(value);

        if (socialType == null) {
            throw new BusinessException(ErrorType.INVALID_SOCIAL_TYPE_ERROR);
        }

        return socialType;
    }
}
