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
public enum FilterCategory {
    RESTAURANT_FEATURE,
    CAFE_FEATURE,
    VISIT_PURPOSE,
    COMPANION_TYPE;

    private static final Map<String, FilterCategory> FILTER_CATEGORY_MAP = new HashMap<>();

    static {
        for (FilterCategory filterCategory : FilterCategory.values()) {
            FILTER_CATEGORY_MAP.put(filterCategory.name(), filterCategory);
        }
    }

    public static FilterCategory fromValue(String value) {
        FilterCategory category = FILTER_CATEGORY_MAP.get(value);

        if (category == null) {
            throw new BusinessException(ErrorType.INVALID_SPOT_TYPE_ERROR);
        }

        return category;
    }

}
