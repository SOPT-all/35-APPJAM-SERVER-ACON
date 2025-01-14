package com.acon.server.member.infra.entity;

import com.acon.server.global.entity.BaseTimeEntity;
import com.acon.server.member.domain.enums.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Column(name = "social_id", nullable = false, unique = true)
    private String socialId;

    @Column(name = "recent_latitude")
    private Double recentLatitude;

    @Column(name = "recent_longitude")
    private Double recentLongitude;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "nickname_updated_at")
    private LocalDate nicknameUpdatedAt;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "left_acorn_count", nullable = false, columnDefinition = "integer default 0")
    private int leftAcornCount;

    @Builder
    public MemberEntity(
            Long id,
            SocialType socialType,
            String socialId,
            Double recentLatitude,
            Double recentLongitude,
            String profileImage,
            String nickname,
            LocalDate nicknameUpdatedAt,
            LocalDate birthDate,
            int leftAcornCount
    ) {
        this.id = id;
        this.socialType = socialType;
        this.socialId = socialId;
        this.recentLatitude = recentLatitude;
        this.recentLongitude = recentLongitude;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.nicknameUpdatedAt = nicknameUpdatedAt;
        this.birthDate = birthDate;
        this.leftAcornCount = leftAcornCount;
    }
}
