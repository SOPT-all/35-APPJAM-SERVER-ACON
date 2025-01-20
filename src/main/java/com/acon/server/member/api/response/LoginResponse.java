package com.acon.server.member.api.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {

    public static LoginResponse of(
            final String accessToken,
            final String refreshToken
    ) {
        return new LoginResponse(accessToken, refreshToken);
    }
}
