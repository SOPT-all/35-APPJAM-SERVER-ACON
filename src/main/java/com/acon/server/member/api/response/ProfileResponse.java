package com.acon.server.member.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
@JsonInclude(Include.NON_NULL)
public record ProfileResponse(
        @NotNull
        String image,
        @NotNull
        String nickname,
        int leftAcornCount,
        String birthDate,
        @NotNull
        List<VerifiedArea> verifiedArea
) {

    public record VerifiedArea(
            @NonNull
            Long id,
            @NonNull
            String name
    ) {

    }
}