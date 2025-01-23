package com.acon.server.member.infra.external.ios;

public record ApplePublicKey(
        String kty,
        String kid,
        String alg,
        String n,
        String e
) {

}
