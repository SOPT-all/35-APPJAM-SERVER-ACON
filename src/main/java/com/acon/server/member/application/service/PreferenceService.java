package com.acon.server.member.application.service;

import com.acon.server.member.api.request.PreferenceRequest;
import com.acon.server.member.application.mapper.PreferenceMapper;
import com.acon.server.member.domain.entity.Preference;
import com.acon.server.member.infra.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceMapper preferenceMapper;
    private final PreferenceRepository preferenceRepository;

    public void createPreference(final PreferenceRequest request, Long memberId) {
        Preference preference = Preference.builder().memberId(memberId).dislikeFoodList(request.dislikeFoodList())
                .favoriteCuisineRank(request.favoriteCuisineRank()).favoriteSpotType(request.favoriteSpotType())
                .favoriteSpotStyle(request.favoriteSpotStyle())
                .favoriteSpotRank(request.favoriteSpotRank()).build();
        preferenceRepository.save(preferenceMapper.toEntity(preference));
    }

}
