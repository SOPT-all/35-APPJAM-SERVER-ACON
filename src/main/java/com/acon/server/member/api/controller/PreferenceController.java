package com.acon.server.member.api.controller;

import com.acon.server.member.api.request.PreferenceRequest;
import com.acon.server.member.application.service.PreferenceService;
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

    @PostMapping()
    public ResponseEntity<Void> postPreference(@Valid @RequestBody final PreferenceRequest request) {
        // 토큰으로 memberId 가져오기
        preferenceService.createPreference(request, 123L);
        return ResponseEntity.ok().build();
    }
}
