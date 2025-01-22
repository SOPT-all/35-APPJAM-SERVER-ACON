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
public enum VisitPurpose {
    MEETING,
    STUDY;

    private static final Map<String, VisitPurpose> VISIT_PURPOSE_MAP = new HashMap<>();

    static {
        for (VisitPurpose visitPurpose : VisitPurpose.values()) {
            VISIT_PURPOSE_MAP.put(visitPurpose.name(), visitPurpose);
        }
    }

    public static VisitPurpose fromValue(String value) {
        VisitPurpose purpose = VISIT_PURPOSE_MAP.get(value);

        if (purpose == null) {
            throw new BusinessException(ErrorType.INVALID_SPOT_TYPE_ERROR);
        }

        return purpose;
    }
}
