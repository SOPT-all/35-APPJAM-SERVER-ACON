package com.acon.server.member.domain.entity;

import com.acon.server.member.domain.enums.SocialType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Member {

    private final Long id;
    private final SocialType socialType;
    private final String socialId;

    private Double recentLatitude;
    private Double recentLongitude;
    private String profileImage;
    private String nickname;
    private LocalDate nicknameUpdatedAt;
    private LocalDate birthDate;
    private int leftAcornCount;

    @Builder
    public Member(
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
