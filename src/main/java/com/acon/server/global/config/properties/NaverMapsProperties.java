package com.acon.server.global.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "naver.maps")
public class NaverMapsProperties {

    private String clientId;
    private String clientSecret;
}
