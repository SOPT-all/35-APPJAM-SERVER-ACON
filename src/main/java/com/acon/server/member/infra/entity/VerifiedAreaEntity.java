package com.acon.server.member.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "verified_area",
        indexes = @Index(
                name = "idx_verified_area_member_id",
                columnList = "member_id"
        )
        // TODO: memberId와 name을 unique로 묶던가 verifiedDate를 위한 테이블을 분리하던가
)
public class VerifiedAreaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "name", nullable = false)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "verified_date", nullable = false)
    private List<LocalDate> verifiedDate;

    @Column(name = "certified_for_three_months", nullable = false)
    private boolean certifiedForThreeMonths;

    @Column(name = "has_certification_mark", nullable = false)
    private boolean hasCertificationMark;

    @Builder
    public VerifiedAreaEntity(
            Long id,
            Long memberId,
            String name,
            List<LocalDate> verifiedDate
    ) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.verifiedDate = verifiedDate;
        this.certifiedForThreeMonths = false;
        this.hasCertificationMark = false;
    }
}
