package com.acon.server.member.infra.external.google.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google")
@RequiredArgsConstructor
@Getter
@Setter
public class GoogleConfig {

    private final String clientId;
}