package com.acon.server.spot.domain.entity;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OpeningHour {

    private final Long id;
    private final Long spotId;

    private DayOfWeek dayOfWeek;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Builder
    public OpeningHour(
            Long id,
            Long spotId,
            DayOfWeek dayOfWeek,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        this.id = id;
        this.spotId = spotId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
