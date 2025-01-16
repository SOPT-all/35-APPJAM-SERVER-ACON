package com.acon.server.member.api.controller;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.member.api.request.LoginRequest;
import com.acon.server.member.api.request.PreferenceRequest;
import com.acon.server.member.api.response.LoginResponse;
import com.acon.server.member.application.service.MemberService;
import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.member.domain.enums.SpotType;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    MemberService memberService;
    private static final int FAVORITE_CUISINE_RANK_SIZE = 3;
    private static final int FAVORITE_SPOT_RANK_SIZE = 4;

    @PostMapping
    public ResponseEntity<Void> postPreference(@Valid @RequestBody final PreferenceRequest request) {
        // ToDo 토큰으로 memberId 가져오기
        List<DislikeFood> dislikeFoodList = request.dislikeFoodList().stream().map(DislikeFood::fromValue).toList();
        if (request.favoriteCuisineRank().size() != FAVORITE_CUISINE_RANK_SIZE) {
            throw new BusinessException(ErrorType.INVALID_CUISINE_ERROR);
        }
        List<Cuisine> favoriteCuisineList = request.favoriteCuisineRank().stream().map(Cuisine::fromValue).toList();
        SpotType favoriteSpotType = SpotType.fromValue(request.favoriteSpotType());
        SpotStyle favoriteSpotStyle = SpotStyle.fromValue(request.favoriteSpotStyle());
        if (request.favoriteSpotRank().size() != FAVORITE_SPOT_RANK_SIZE) {
            throw new BusinessException(ErrorType.INVALID_FAVORITE_SPOT_ERROR);
        }
        List<FavoriteSpot> favoriteSpotRank = request.favoriteSpotRank().stream().map(FavoriteSpot::fromValue).toList();

        memberService.createPreference(dislikeFoodList, favoriteCuisineList, favoriteSpotType, favoriteSpotStyle,
                favoriteSpotRank, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody final LoginRequest request) {
        return ResponseEntity.ok(
                memberService.login(request)
        );
    }

}
