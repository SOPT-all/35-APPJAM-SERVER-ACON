package com.acon.server.member.domain.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VerifiedArea {

    private final Long id;
    private final Long memberId;
    private final String name;

    private List<LocalDate> verifiedDate;
    private boolean certifiedForThreeMonths;
    private boolean hasCertificationMark;

    @Builder
    public VerifiedArea(
            Long id,
            Long memberId,
            String name,
            List<LocalDate> verifiedDate,
            boolean certifiedForThreeMonths,
            boolean hasCertificationMark
    ) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.verifiedDate = verifiedDate;
        this.certifiedForThreeMonths = certifiedForThreeMonths;
        this.hasCertificationMark = hasCertificationMark;
    }
}
