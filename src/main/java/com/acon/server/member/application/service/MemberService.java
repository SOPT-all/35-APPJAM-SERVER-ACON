package com.acon.server.member.application.service;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.global.external.NaverMapsClient;
import com.acon.server.member.infra.entity.RecentGuidedSpotEntity;
import com.acon.server.member.infra.entity.VerifiedAreaEntity;
import com.acon.server.member.infra.repository.RecentGuidedSpotRepository;
import com.acon.server.member.infra.repository.VerifiedAreaRepository;
import com.acon.server.spot.infra.repository.SpotRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Value("${naver.apiKeyId}")
    private String apiKeyId;

    @Value("${naver.apiKey}")
    private String apiKey;

    private final RecentGuidedSpotRepository recentGuidedSpotRepository;
    private final SpotRepository spotRepository;
    private final VerifiedAreaRepository verifiedAreaRepository;
    private final NaverMapsClient naverMapsClient;

    public void createGuidedSpot(final Long spotId, final Long memberId) {
        if (spotRepository.existsById(spotId)) {
            throw new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR);
        }

        RecentGuidedSpotEntity recentGuidedSpotEntity = RecentGuidedSpotEntity.builder()
                .memberId(memberId)
                .spotId(spotId)
                .build();
        recentGuidedSpotRepository.save(recentGuidedSpotEntity);
    }

    public String createMemberArea(final Double latitude, final Double longitude, final Long memberId) {
        Map<String, Object> response = naverMapsClient.getReverseGeocode(apiKeyId,
                apiKey,
                longitude + "," + latitude, "admcode", "json");
        String adminDong = extractAreaName(response);
        verifiedAreaRepository.save(
                VerifiedAreaEntity.builder().name(adminDong).memberId(memberId)
                        .verifiedDate(Collections.singletonList(LocalDate.now())).build()
        );
        return adminDong;
    }

    private String extractAreaName(Map<String, Object> response) {
        try {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            if (results != null && !results.isEmpty()) {
                Map<String, Object> region = (Map<String, Object>) results.get(0).get("region");
                if (region != null) {
                    Map<String, Object> area3 = (Map<String, Object>) region.get("area3");
                    if (area3 != null) {
                        return (String) area3.get("name");
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR);
        }
        throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR);
    }
}
