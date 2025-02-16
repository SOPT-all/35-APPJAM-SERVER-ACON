package com.acon.server.member.api.response;

public record MemberAreaResponse(
        Long id,
        String name
) {

    public static MemberAreaResponse of(
            final Long id,
            final String name
    ) {
        return new MemberAreaResponse(id, name);
    }
}
