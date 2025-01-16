package com.acon.server.member.api.controller;

import com.acon.server.member.api.request.LoginRequest;
import com.acon.server.member.api.response.AcornCountResponse;
import com.acon.server.member.api.response.LoginResponse;
import com.acon.server.member.application.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody final LoginRequest request) {
        return ResponseEntity.ok(
                memberService.login(request)
        );
    }

    @GetMapping("/member/acorn")
    public ResponseEntity<AcornCountResponse> getAcornCount() {
        // TODO: 토큰으로 memberId 가져오기
        return ResponseEntity.ok(
                memberService.fetchAcornCount(1L)
        );
    }
}
