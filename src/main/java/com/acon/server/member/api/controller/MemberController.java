package com.acon.server.member.api.controller;

import com.acon.server.member.api.request.GuidedSpotRequest;
import com.acon.server.member.api.request.LoginRequest;
import com.acon.server.member.api.request.MemberAreaRequest;
import com.acon.server.member.api.request.PreferenceRequest;
import com.acon.server.member.api.response.AcornCountResponse;
import com.acon.server.member.api.response.LoginResponse;
import com.acon.server.member.api.response.MemberAreaResponse;
import com.acon.server.member.application.service.MemberService;
import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.spot.domain.enums.SpotType;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    @PostMapping(path = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody final LoginRequest request
    ) {
        return ResponseEntity.ok(
                memberService.login(request)
        );
    }

    @PostMapping(path = "/member/preference", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postPreference(
            @Valid @RequestBody final PreferenceRequest request
    ) {
        // TODO: 토큰으로 memberId 가져오기
        List<DislikeFood> dislikeFoodList = request.dislikeFoodList().stream().map(DislikeFood::fromValue).toList();
        List<Cuisine> favoriteCuisineList = request.favoriteCuisineRank().stream().map(Cuisine::fromValue).toList();
        SpotType favoriteSpotType = SpotType.fromValue(request.favoriteSpotType());
        SpotStyle favoriteSpotStyle = SpotStyle.fromValue(request.favoriteSpotStyle());
        List<FavoriteSpot> favoriteSpotRank = request.favoriteSpotRank().stream().map(FavoriteSpot::fromValue).toList();

        memberService.createPreference(dislikeFoodList, favoriteCuisineList, favoriteSpotType, favoriteSpotStyle,
                favoriteSpotRank, 1L);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/member/area", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberAreaResponse> postArea(
            @Valid @RequestBody final MemberAreaRequest request
    ) {
        // TODO: 토큰 검증 이후 MemberID 추출 필요
        String area = memberService.createMemberArea(request.latitude(), request.longitude(), 1L);

        return ResponseEntity.ok(new MemberAreaResponse(area));
    }

    // TODO: Member 도메인에 있어야 할까? 고민 필요
    @PostMapping(path = "/member/guided-spot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postGuidedSpot(
            @Valid @RequestBody final GuidedSpotRequest request
    ) {
        // TODO: 토큰 검증 이후 MemberID 추출 필요
        memberService.createGuidedSpot(request.spotId(), 1L);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/member/acorn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcornCountResponse> getAcornCount() {
        // TODO: 토큰으로 memberId 가져오기
        return ResponseEntity.ok(
                memberService.fetchAcornCount(1L)
        );
    }
}
