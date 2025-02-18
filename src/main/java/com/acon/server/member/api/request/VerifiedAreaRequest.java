package com.acon.server.member.api.request;

// TODO: validation 추가
public record VerifiedAreaRequest(
        Double latitude,
        Double longitude
) {

}
