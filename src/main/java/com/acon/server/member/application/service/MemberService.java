package com.acon.server.member.application.service;

import com.acon.server.common.auth.jwt.JwtUtils;
import com.acon.server.member.api.request.LoginRequest;
import com.acon.server.member.api.response.LoginResponse;
import com.acon.server.member.application.mapper.MemberMapper;
import com.acon.server.member.application.mapper.PreferenceMapper;
import com.acon.server.member.domain.entity.Member;
import com.acon.server.member.domain.entity.Preference;
import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.SocialType;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.member.domain.enums.SpotType;
import com.acon.server.member.infra.entity.MemberEntity;
import com.acon.server.member.infra.external.google.GoogleSocialService;
import com.acon.server.member.infra.repository.MemberRepository;
import com.acon.server.member.infra.repository.PreferenceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final GoogleSocialService googleSocialService;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final JwtUtils jwtUtils;
    private final PreferenceMapper preferenceMapper;
    private final PreferenceRepository preferenceRepository;

    public void createPreference(List<DislikeFood> dislikeFoodList, List<Cuisine> favoriteCuisineList,
                                 SpotType favoriteSpotType,
                                 SpotStyle favoriteSpotStyle, List<FavoriteSpot> favoriteSpotRank, Long memberId) {
        Preference preference = Preference.builder().memberId(memberId).dislikeFoodList(dislikeFoodList)
                .favoriteCuisineRank(favoriteCuisineList).favoriteSpotType(favoriteSpotType)
                .favoriteSpotStyle(favoriteSpotStyle)
                .favoriteSpotRank(favoriteSpotRank).build();
        preferenceRepository.save(preferenceMapper.toEntity(preference));
    }

    @Transactional
    public LoginResponse login(final LoginRequest request) {
        String socialId = googleSocialService.login(request.idToken());
        Long memberId = fetchMemberId(request.socialType(), socialId);
        List<String> tokens = jwtUtils.createToken(memberId);
        return LoginResponse.of(tokens.get(0), tokens.get(1));
    }

    private boolean isExistingMember(
            final SocialType socialType,
            final String socialId
    ) {
        return memberRepository.findBySocialTypeAndSocialId(socialType, socialId).isPresent();
    }

    protected Long fetchMemberId(final SocialType socialType, final String socialId) {
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

}
