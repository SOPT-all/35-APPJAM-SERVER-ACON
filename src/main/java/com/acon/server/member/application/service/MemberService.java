package com.acon.server.member.application.service;

import com.acon.server.common.auth.jwt.JwtUtils;
import com.acon.server.member.api.request.LoginRequest;
import com.acon.server.member.api.response.AcornCountResponse;
import com.acon.server.member.api.response.LoginResponse;
import com.acon.server.member.application.mapper.MemberMapper;
import com.acon.server.member.domain.entity.Member;
import com.acon.server.member.domain.enums.SocialType;
import com.acon.server.member.infra.entity.MemberEntity;
import com.acon.server.member.infra.external.google.GoogleSocialService;
import com.acon.server.member.infra.repository.MemberRepository;
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

    @Transactional
    public LoginResponse login(final LoginRequest request) {
        String socialId = googleSocialService.login(request.idToken());
        Long memberId = fetchMemberId(request.socialType(), socialId);
        List<String> tokens = jwtUtils.createToken(memberId);
        return LoginResponse.of(tokens.get(0), tokens.get(1));
    }

    public AcornCountResponse fetchAcornCount(final Long memberId) {
        MemberEntity memberEntity = memberRepository.findByIdOrElseThrow(memberId);
        int acornCount = memberEntity.getLeftAcornCount();

        return new AcornCountResponse(acornCount);
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
