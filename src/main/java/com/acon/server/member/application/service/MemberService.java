package com.acon.server.member.application.service;

import com.acon.server.global.auth.MemberAuthentication;
import com.acon.server.global.auth.PrincipalHandler;
import com.acon.server.global.auth.jwt.JwtTokenProvider;
import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.global.external.NaverMapsAdapter;
import com.acon.server.member.api.response.AcornCountResponse;
import com.acon.server.member.api.response.LoginResponse;
import com.acon.server.member.application.mapper.GuidedSpotMapper;
import com.acon.server.member.application.mapper.MemberMapper;
import com.acon.server.member.application.mapper.PreferenceMapper;
import com.acon.server.member.application.mapper.VerifiedAreaMapper;
import com.acon.server.member.domain.entity.GuidedSpot;
import com.acon.server.member.domain.entity.Member;
import com.acon.server.member.domain.entity.Preference;
import com.acon.server.member.domain.entity.VerifiedArea;
import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.SocialType;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.member.infra.entity.GuidedSpotEntity;
import com.acon.server.member.infra.entity.MemberEntity;
import com.acon.server.member.infra.entity.VerifiedAreaEntity;
import com.acon.server.member.infra.external.google.GoogleSocialService;
import com.acon.server.member.infra.external.ios.AppleAuthAdapter;
import com.acon.server.member.infra.repository.GuidedSpotRepository;
import com.acon.server.member.infra.repository.MemberRepository;
import com.acon.server.member.infra.repository.PreferenceRepository;
import com.acon.server.member.infra.repository.VerifiedAreaRepository;
import com.acon.server.spot.domain.enums.SpotType;
import com.acon.server.spot.infra.repository.SpotRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final GuidedSpotRepository guidedSpotRepository;
    private final MemberRepository memberRepository;
    private final PreferenceRepository preferenceRepository;
    private final VerifiedAreaRepository verifiedAreaRepository;
    private final SpotRepository spotRepository;

    private final GuidedSpotMapper guidedSpotMapper;
    private final MemberMapper memberMapper;
    private final PreferenceMapper preferenceMapper;
    private final VerifiedAreaMapper verifiedAreaMapper;

    private final JwtTokenProvider jwtTokenProvider;
    private final PrincipalHandler principalHandler;
    private final GoogleSocialService googleSocialService;
    private final AppleAuthAdapter appleAuthService;

    private final NaverMapsAdapter naverMapsAdapter;

    // TODO: 메서드 순서 정리, TRANSACTION 설정, mapper 사용
    // TODO: @Valid 거친 건 원시타입으로 받기

    @Transactional
    public LoginResponse login(
            final SocialType socialType,
            final String idToken
    ) {
        String socialId;

        if (socialType == SocialType.GOOGLE) {
            socialId = googleSocialService.login(idToken);
        } else if (socialType == SocialType.APPLE) {
            socialId = appleAuthService.getAppleAccountId(idToken);
        } else {
            throw new BusinessException(ErrorType.INVALID_SOCIAL_TYPE_ERROR);
        }

        Long memberId = fetchMemberId(socialType, socialId);
        MemberAuthentication memberAuthentication = new MemberAuthentication(memberId, null, null);
        String accessToken = jwtTokenProvider.issueAccessToken(memberAuthentication);
        String refreshToken = jwtTokenProvider.issueRefreshToken();

        return LoginResponse.of(accessToken, refreshToken);
    }

    private boolean isExistingMember(
            final SocialType socialType,
            final String socialId
    ) {
        return memberRepository.findBySocialTypeAndSocialId(socialType, socialId).isPresent();
    }

    protected Long fetchMemberId(
            final SocialType socialType,
            final String socialId
    ) {
        MemberEntity memberEntity;

        if (isExistingMember(socialType, socialId)) {
            memberEntity = memberRepository.findBySocialTypeAndSocialId(
                    socialType,
                    socialId
            ).orElse(null);
        } else {
            memberEntity = memberRepository.save(MemberEntity.builder()
                    .socialType(socialType)
                    .socialId(socialId)
                    .leftAcornCount(25)
                    .build());
        }

        Member member = memberMapper.toDomain(memberEntity);

        return member.getId();
    }

    @Transactional
    public String createMemberArea(
            final Double latitude,
            final Double longitude
    ) {
        MemberEntity memberEntity = memberRepository.findByIdOrElseThrow(principalHandler.getUserIdFromPrincipal());
        String legalDong = naverMapsAdapter.getReverseGeoCodingResult(latitude, longitude);
        Optional<VerifiedAreaEntity> optionalVerifiedAreaEntity = verifiedAreaRepository.findByMemberIdAndName(
                memberEntity.getId(), legalDong);

        optionalVerifiedAreaEntity.ifPresentOrElse(
                verifiedAreaEntity -> {
                    VerifiedArea verifiedArea = verifiedAreaMapper.toDomain(verifiedAreaEntity);
                    List<LocalDate> dates = verifiedArea.getVerifiedDate();
                    dates.add(LocalDate.now());
                    verifiedArea.setVerifiedDate(dates);
                    verifiedAreaRepository.save(verifiedAreaMapper.toEntity(verifiedArea));
                },
                () -> verifiedAreaRepository.save(
                        VerifiedAreaEntity.builder()
                                .name(legalDong)
                                .memberId(memberEntity.getId())
                                .verifiedDate(Collections.singletonList(LocalDate.now()))
                                .build()
                )
        );

        return legalDong;
    }

    @Transactional
    public void createPreference(
            final List<DislikeFood> dislikeFoodList,
            final List<Cuisine> favoriteCuisineList,
            final SpotType favoriteSpotType,
            final SpotStyle favoriteSpotStyle,
            final List<FavoriteSpot> favoriteSpotRank
    ) {
        MemberEntity memberEntity = memberRepository.findByIdOrElseThrow(principalHandler.getUserIdFromPrincipal());

        Preference preference = Preference.builder()
                .memberId(memberEntity.getId())
                .dislikeFoodList(dislikeFoodList)
                .favoriteCuisineRank(favoriteCuisineList)
                .favoriteSpotType(favoriteSpotType)
                .favoriteSpotStyle(favoriteSpotStyle)
                .favoriteSpotRank(favoriteSpotRank)
                .build();

        preferenceRepository.save(preferenceMapper.toEntity(preference));
    }

    @Transactional
    public void createGuidedSpot(final Long spotId) {
        if (!spotRepository.existsById(spotId)) {
            throw new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR);
        }

        MemberEntity memberEntity = memberRepository.findByIdOrElseThrow(principalHandler.getUserIdFromPrincipal());
        Optional<GuidedSpotEntity> optionalGuidedSpotEntity =
                guidedSpotRepository.findByMemberIdAndSpotId(memberEntity.getId(), spotId);

        optionalGuidedSpotEntity.ifPresentOrElse(
                guidedSpotEntity -> {
                    GuidedSpot guidedSpot = guidedSpotMapper.toDomain(guidedSpotEntity);
                    guidedSpot.setUpdatedAt(LocalDateTime.now());
                    guidedSpotRepository.save(guidedSpotMapper.toEntity(guidedSpot));
                },
                () -> guidedSpotRepository.save(
                        GuidedSpotEntity.builder()
                                .memberId(memberEntity.getId())
                                .spotId(spotId)
                                .build()
                )
        );
    }

    @Transactional(readOnly = true)
    public AcornCountResponse fetchAcornCount() {
        MemberEntity memberEntity = memberRepository.findByIdOrElseThrow(principalHandler.getUserIdFromPrincipal());
        int acornCount = memberEntity.getLeftAcornCount();

        return new AcornCountResponse(acornCount);
    }

    // TODO: 최근 길 안내 장소 지우는 스케줄러 추가
}
