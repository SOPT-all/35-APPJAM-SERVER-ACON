package com.acon.server.member.application.service;

import com.acon.server.member.application.mapper.PreferenceMapper;
import com.acon.server.member.domain.entity.Preference;
import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.member.domain.enums.SpotType;
import com.acon.server.member.infra.repository.PreferenceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreferenceService {

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

}
