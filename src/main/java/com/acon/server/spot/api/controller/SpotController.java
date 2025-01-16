package com.acon.server.spot.api.controller;

import com.acon.server.spot.api.response.MenuListResponse;
import com.acon.server.spot.application.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class SpotController {

    private final SpotService spotService;

    @GetMapping("/spot/{spotId}/menus")
    public ResponseEntity<MenuListResponse> getMenus(
            @PathVariable(name = "spotId") Long spotId
    ) {
        return ResponseEntity.ok(spotService.fetchMenus(spotId));
    }
}
