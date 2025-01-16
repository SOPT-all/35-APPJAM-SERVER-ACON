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
public enum Day {

    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT,
    SUN,
    ;

    private static final Map<String, Day> DAY_MAP = new HashMap<>();

    static {
        for (Day day : Day.values()) {
            DAY_MAP.put(day.name(), day);
        }
    }

    public static Day fromValue(String value) {
        Day day = DAY_MAP.get(value);

        if (day == null) {
            throw new BusinessException(ErrorType.INVALID_DAY_ERROR);
        }

        return day;
    }
}
