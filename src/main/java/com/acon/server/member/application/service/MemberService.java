package com.acon.server.member.application.service;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.member.infra.entity.RecentGuidedSpotEntity;
import com.acon.server.member.infra.repository.RecentGuidedSpotRepository;
import com.acon.server.spot.infra.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final RecentGuidedSpotRepository recentGuidedSpotRepository;
    private final SpotRepository spotRepository;

    public void createGuidedSpot(final Long spotId, final Long memberId) {
        spotRepository.findById(spotId).orElseThrow(() -> new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR));
        recentGuidedSpotRepository.save(RecentGuidedSpotEntity.builder().memberId(memberId).spotId(spotId).build());
    }

}
