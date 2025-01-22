package com.acon.server.common.auth.jwt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

// TODO: 스프링 빈 등록 방식 통일 및 본 파일 네이밍, 위치 변경
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;
}
