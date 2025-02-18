package com.acon.server.member.api.response;

import java.util.List;

public record VerifiedAreaListResponse(
        List<VerifiedAreaResponse> verifiedAreaList
) {

}
