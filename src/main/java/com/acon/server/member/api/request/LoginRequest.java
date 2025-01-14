package com.acon.server.member.api.request;

import com.acon.server.member.domain.enums.SocialType;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        SocialType socialType,
        @NotNull
        String idToken
) {

}
