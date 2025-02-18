package com.acon.server.member.api.response;

import java.util.List;

public record VerifiedAreaListResponse(
        List<VerifiedArea> verifiedAreaList
) {

    public record VerifiedArea(
            Long id,
            String name
    ) {

    }

}
