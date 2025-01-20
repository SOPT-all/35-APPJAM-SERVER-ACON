package com.acon.server.member.api.request;

// TODO: validation 추가
public record MemberAreaRequest(
        Double latitude,
        Double longitude
) {

}
