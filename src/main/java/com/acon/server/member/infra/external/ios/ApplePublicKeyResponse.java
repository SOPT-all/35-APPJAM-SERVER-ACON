package com.acon.server.member.infra.external.ios;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import java.util.List;

public record ApplePublicKeyResponse(
        List<ApplePublicKey> keys
) {

    public ApplePublicKey getMatchedKey(String kid, String alg) {
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorType.INTERNAL_SERVER_ERROR));
    }
}
