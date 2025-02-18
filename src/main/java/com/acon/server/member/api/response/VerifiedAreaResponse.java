package com.acon.server.member.api.response;

public record VerifiedAreaResponse(
        Long id,
        String name
) {

    public static VerifiedAreaResponse of(
            final Long id,
            final String name
    ) {
        return new VerifiedAreaResponse(id, name);
    }
}
