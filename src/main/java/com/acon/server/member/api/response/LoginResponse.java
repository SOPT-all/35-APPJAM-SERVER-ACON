package com.acon.server.member.api.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {

}
