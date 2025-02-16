package com.acon.server.member.api.controller;

import com.acon.server.member.api.request.GuidedSpotRequest;
import com.acon.server.member.api.request.LoginRequest;
import com.acon.server.member.api.request.LogoutRequest;
import com.acon.server.member.api.request.MemberAreaRequest;
import com.acon.server.member.api.request.PreferenceRequest;
import com.acon.server.member.api.request.ReissueTokenRequest;
import com.acon.server.member.api.request.WithdrawalReasonRequest;
import com.acon.server.member.api.response.AcornCountResponse;
import com.acon.server.member.api.response.AreaResponse;
import com.acon.server.member.api.response.LoginResponse;
import com.acon.server.member.api.response.MemberAreaResponse;
import com.acon.server.member.api.response.PreSignedUrlResponse;
import com.acon.server.member.api.response.ProfileResponse;
import com.acon.server.member.api.response.ReissueTokenResponse;
import com.acon.server.member.application.service.MemberService;
import com.acon.server.member.domain.enums.Cuisine;
import com.acon.server.member.domain.enums.DislikeFood;
import com.acon.server.member.domain.enums.FavoriteSpot;
import com.acon.server.member.domain.enums.ImageType;
import com.acon.server.member.domain.enums.SocialType;
import com.acon.server.member.domain.enums.SpotStyle;
import com.acon.server.spot.domain.enums.SpotType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        return ResponseEntity.ok(
                memberService.createMemberArea(request.latitude(), request.longitude())
        );
    }

    @GetMapping(path = "/area", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AreaResponse> getArea(
            @DecimalMin(value = "33.1", message = "위도는 최소 33.1°N 이상이어야 합니다.(대한민국 기준)")
            @DecimalMax(value = "38.6", message = "위도는 최대 38.6°N 이하이어야 합니다.(대한민국 기준)")
            @Validated @RequestParam(name = "latitude") final Double latitude,
            @DecimalMin(value = "124.6", message = "경도는 최소 124.6°E 이상이어야 합니다.(대한민국 기준)")
            @DecimalMax(value = "131.9", message = "경도는 최대 131.9°E 이하이어야 합니다.(대한민국 기준)")
            @Validated @RequestParam(name = "longitude") final Double longitude
    ) {
        String area = memberService.fetchMemberArea(latitude, longitude);

        return ResponseEntity.ok(new AreaResponse(area));
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

    @GetMapping(path = "/members/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok(
                memberService.fetchProfile()
        );
    }

    @GetMapping(path = "/images/presigned-url", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PreSignedUrlResponse> getPreSignedUrl(
            @RequestParam(name = "imageType") final String imageTypeString
    ) {
        ImageType imageType = ImageType.fromValue(imageTypeString);

        return ResponseEntity.ok(
                memberService.fetchPreSignedUrl(imageType)
        );
    }

    @PostMapping(path = "/auth/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> logout(
            @Valid @RequestBody LogoutRequest request
    ) {
        memberService.logout(request.refreshToken());

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/auth/reissue",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ReissueTokenResponse> reissueToken(
            @Valid @RequestBody ReissueTokenRequest request
    ) {
        return ResponseEntity.ok(
                memberService.reissueToken(request.refreshToken())
        );
    }

    @PostMapping(path = "/members/withdrawal", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postWithdrawal(
            @Valid @RequestBody WithdrawalReasonRequest request
    ) {
        memberService.withdrawMember(request.reason(), request.refreshToken());

        return ResponseEntity.ok().build();
    }
}
