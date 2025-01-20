package com.acon.server.member.application.service;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.member.infra.entity.GuidedSpotEntity;
import com.acon.server.member.infra.repository.GuidedSpotRepository;
import com.acon.server.spot.infra.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final GuidedSpotRepository guidedSpotRepository;
    private final SpotRepository spotRepository;

    @Transactional
    public void createGuidedSpot(final Long spotId, final Long memberId) {
        if (!spotRepository.existsById(spotId)) {
            throw new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR);
        }

        GuidedSpotEntity guidedSpotEntity = GuidedSpotEntity.builder()
                .memberId(memberId)
                .spotId(spotId)
                .build();
        guidedSpotRepository.save(guidedSpotEntity);
    }
}
