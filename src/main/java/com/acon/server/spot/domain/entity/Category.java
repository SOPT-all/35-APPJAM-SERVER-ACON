package com.acon.server.spot.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Category {

    private final Long id;
    private final String name;

    @Builder
    public Category(
            Long id,
            String name
    ) {
        this.id = id;
        this.name = name;
    }
}
