package com.acon.server.member.api.controller;

import com.acon.server.member.api.request.PreferenceRequest;
import com.acon.server.member.application.service.PreferenceService;
import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.member.domain.enums.SpotType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/preference")
public class PreferenceController {

    private final PreferenceService preferenceService;

    @PostMapping
    public ResponseEntity<Void> postPreference(@Valid @RequestBody final PreferenceRequest request) {
        // ToDo 토큰으로 memberId 가져오기
        preferenceService.createPreference(request, 1L);
        request.dislikeFoodList().forEach(dislikeFood -> DislikeFood.fromValue(String.valueOf(dislikeFood)));
        request.favoriteCuisineRank().forEach(cuisine -> Cuisine.fromValue(String.valueOf(cuisine)));
        SpotType.fromValue(String.valueOf(request.favoriteSpotType()));
        SpotStyle.fromValue(String.valueOf(request.favoriteSpotStyle()));
        request.favoriteSpotRank().forEach(favoriteSpot -> FavoriteSpot.fromValue(String.valueOf(favoriteSpot)));
        return ResponseEntity.ok().build();
    }
}
