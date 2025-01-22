package com.acon.server.member.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// TODO: 세부 메시지 추가
public record LoginRequest(
        @NotBlank(message = "소셜 로그인 종류는 공백일 수 없습니다.")
        String socialType,
        @NotNull
        @NotBlank
        String idToken
) {

}
