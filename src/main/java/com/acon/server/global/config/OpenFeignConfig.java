package com.acon.server.global.config;

import com.acon.server.ServerApplication;
import feign.Retryer;
import feign.Retryer.Default;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = ServerApplication.class)
public class OpenFeignConfig {

    @Bean
    public Retryer retryer() {
        return new Default(1000, 1500, 2);
    }
    // TODO: 타임아웃 설정 추가, 서킷 브레이커 적용하기
}
