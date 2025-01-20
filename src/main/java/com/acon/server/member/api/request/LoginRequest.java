package com.acon.server.member.api.request;

import com.acon.server.member.domain.enums.SocialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// TODO: 세부 메시지 추가
public record LoginRequest(
        @NotNull
        SocialType socialType,
        @NotNull
        @NotBlank
        String idToken
) {

}
