package com.acon.server.spot.domain.entity;

import com.acon.server.spot.domain.enums.Day;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OpeningHour {

    private final Long id;
    private final Long spotId;

    private Day day;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Builder
    public OpeningHour(
            Long id,
            Long spotId,
            Day day,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        this.id = id;
        this.spotId = spotId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
