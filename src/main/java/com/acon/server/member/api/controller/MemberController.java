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
import com.acon.server.member.domain.enums.SocialType;
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

    @PostMapping(
            path = "/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody final LoginRequest request
    ) {
        SocialType socialType = SocialType.fromValue(request.socialType());

        return ResponseEntity.ok(
                memberService.login(socialType, request.idToken())
        );
    }

    @PostMapping(path = "/member/area",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MemberAreaResponse> postArea(
            @Valid @RequestBody final MemberAreaRequest request
    ) {
        String area = memberService.createMemberArea(request.latitude(), request.longitude());

        return ResponseEntity.ok(new MemberAreaResponse(area));
    }

    @PostMapping(path = "/member/preference", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postPreference(
            @Valid @RequestBody final PreferenceRequest request
    ) {
        List<DislikeFood> dislikeFoodList = request.dislikeFoodList().stream().map(DislikeFood::fromValue).toList();
        List<Cuisine> favoriteCuisineList = request.favoriteCuisineRank().stream().map(Cuisine::fromValue).toList();
        SpotType favoriteSpotType = SpotType.fromValue(request.favoriteSpotType());
        SpotStyle favoriteSpotStyle = SpotStyle.fromValue(request.favoriteSpotStyle());
        List<FavoriteSpot> favoriteSpotRank = request.favoriteSpotRank().stream().map(FavoriteSpot::fromValue).toList();

        memberService.createPreference(dislikeFoodList, favoriteCuisineList, favoriteSpotType, favoriteSpotStyle,
                favoriteSpotRank);

        return ResponseEntity.ok().build();
    }

    // TODO: Member 도메인에 있어야 할까? 고민 필요
    @PostMapping(path = "/member/guided-spot", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postGuidedSpot(
            @Valid @RequestBody final GuidedSpotRequest request
    ) {
        memberService.createGuidedSpot(request.spotId());

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/member/acorn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcornCountResponse> getAcornCount() {
        return ResponseEntity.ok(
                memberService.fetchAcornCount()
        );
    }
}
