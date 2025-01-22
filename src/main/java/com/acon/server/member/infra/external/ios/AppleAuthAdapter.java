package com.acon.server.member.infra.external.ios;

import com.acon.server.global.auth.jwt.JwtTokenProvider;
import java.security.PublicKey;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppleAuthAdapter {

    private final AppleAuthClient appleAuthClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final JwtTokenProvider jwtTokenProvider;

    public String getAppleAccountId(String identityToken) {
        Map<String, String> headers = jwtTokenProvider.parseHeaders(identityToken);
        PublicKey publicKey =
                applePublicKeyGenerator.generatePublicKey(headers, appleAuthClient.getAppleAuthPublicKey());

        return jwtTokenProvider.getTokenClaims(identityToken, publicKey).getSubject();
    }
}
