package com.acon.server.member.domain.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class VerifiedArea {

    private final Long id;
    private final Long memberId;
    private final String name;

    private List<LocalDate> verifiedDate;
    private boolean certified;
    private boolean hasCertificationMark;

    @Builder
    private VerifiedArea(
            Long id,
            Long memberId,
            String name,
            List<LocalDate> verifiedDate,
            boolean certified,
            boolean hasCertificationMark
    ) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.verifiedDate = verifiedDate;
        this.certified = certified;
        this.hasCertificationMark = hasCertificationMark;
    }
}
