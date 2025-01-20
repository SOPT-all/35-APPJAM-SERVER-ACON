package com.acon.server.spot.domain.enums;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Day {

    MON(MONDAY),
    TUE(TUESDAY),
    WED(WEDNESDAY),
    THU(THURSDAY),
    FRI(FRIDAY),
    SAT(SATURDAY),
    SUN(SUNDAY),
    ;

    private final DayOfWeek dayOfWeek;

    private static final Map<String, Day> DAY_MAP = new HashMap<>();

    static {
        for (Day day : Day.values()) {
            DAY_MAP.put(day.name(), day);
        }
    }

    public static DayOfWeek getDayOfWeekFromValue(String value) {
        Day day = DAY_MAP.get(value.toUpperCase());

        if (day == null) {
            throw new BusinessException(ErrorType.INVALID_DAY_ERROR);
        }

        return day.getDayOfWeek();
    }
}
