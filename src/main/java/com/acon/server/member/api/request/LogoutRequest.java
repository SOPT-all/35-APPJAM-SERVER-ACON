package com.acon.server.member.api.request;

import jakarta.validation.constraints.NotNull;

public record LogoutRequest(
        @NotNull(message = "refreshToken은 필수입니다.")
        String refreshToken
) {

}