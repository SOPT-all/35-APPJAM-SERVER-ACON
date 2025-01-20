package com.acon.server.spot.api.controller;

import com.acon.server.spot.api.response.MenuResponse;
import com.acon.server.spot.api.response.SearchSpotListResponse;
import com.acon.server.spot.api.response.SpotDetailResponse;
import com.acon.server.spot.api.response.VerifiedSpotResponse;
import com.acon.server.spot.application.service.SpotService;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SpotController {

    private final SpotService spotService;

    @GetMapping("/spot/{spotId}")
    public ResponseEntity<SpotDetailResponse> getSpotDetail(
            @Positive(message = "spotId는 양수여야 합니다.")
            @Validated @PathVariable(name = "spotId") final Long spotId
    ) {
        return ResponseEntity.ok(
                spotService.fetchSpotDetail(spotId)
        );
    }

    @GetMapping("/spot/{spotId}/menus")
    public ResponseEntity<List<MenuResponse>> getMenus(
            @Positive(message = "spotId는 양수여야 합니다.")
            @Validated @PathVariable(name = "spotId") final Long spotId
    ) {
        return ResponseEntity.ok(
                spotService.fetchMenus(spotId)
        );
    }

    // TODO: 메서드 네이밍 수정 필요
    @GetMapping("/spots/search")
    public ResponseEntity<SearchSpotListResponse> searchSpot(
            @RequestParam(value = "keyword", required = false) final String keyword
    ) {
        return ResponseEntity.ok(
                spotService.searchSpot(keyword)
        );
    }

    // TODO: 메서드 네이밍 수정 필요
    @GetMapping("/spot/verify")
    public ResponseEntity<VerifiedSpotResponse> verifySpot(
            @Positive(message = "spotId는 양수여야 합니다.")
            @Validated @RequestParam(name = "spotId") Long spotId,
            @Validated @RequestParam(name = "longitude") Double longitude,
            @Validated @RequestParam(name = "latitude") Double latitude
    ) {
        return ResponseEntity.ok(
                new VerifiedSpotResponse(spotService.verifySpot(spotId, longitude, latitude))
        );
    }
}
