package com.acon.server.global.config;

import com.acon.server.spot.application.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppStartupConfig {

    private final SpotService spotService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            log.info("애플리케이션 시작 시 Spot 데이터를 업데이트하는 작업을 시작합니다.");
            spotService.updateNullCoordinatesForSpots();
            log.info("Spot 데이터 업데이트 작업이 완료되었습니다.");
        };
    }
}
