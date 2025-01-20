package com.acon.server.global.external;

import com.acon.server.global.config.properties.NaverMapsProperties;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class NaverMapsFeignConfig {

    private final NaverMapsProperties naverMapsProperties;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-NCP-APIGW-API-KEY-ID", naverMapsProperties.getClientId());
            requestTemplate.header("X-NCP-APIGW-API-KEY", naverMapsProperties.getClientSecret());
        };
    }
}
