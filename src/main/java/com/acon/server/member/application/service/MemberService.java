package com.acon.server.member.application.service;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.member.infra.entity.RecentGuidedSpotEntity;
import com.acon.server.member.infra.repository.RecentGuidedSpotRepository;
import com.acon.server.spot.infra.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final RecentGuidedSpotRepository recentGuidedSpotRepository;
    private final SpotRepository spotRepository;

    @Transactional
    public void createGuidedSpot(final Long spotId, final Long memberId) {
        if (!spotRepository.existsById(spotId)) {
            throw new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR);
        }

        RecentGuidedSpotEntity recentGuidedSpotEntity = RecentGuidedSpotEntity.builder()
                .memberId(memberId)
                .spotId(spotId)
                .build();
        recentGuidedSpotRepository.save(recentGuidedSpotEntity);
    }
}
