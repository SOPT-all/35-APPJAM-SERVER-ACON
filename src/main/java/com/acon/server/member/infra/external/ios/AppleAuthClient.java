package com.acon.server.member.infra.external.ios;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleAuthClient", url = "${apple.auth.public-key-url}")
public interface AppleAuthClient {

    @GetMapping
    ApplePublicKeyResponse getAppleAuthPublicKey();
}
