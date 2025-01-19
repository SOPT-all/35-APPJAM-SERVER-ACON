package com.acon.server.global.external;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverMapsClient", url = "https://naveropenapi.apigw.ntruss.com")
public interface NaverMapsClient {

    @GetMapping(value = "/map-reversegeocode/v2/gc", headers = "Accept=application/json")
    Map<String, Object> getReverseGeocode(
            @RequestHeader("X-NCP-APIGW-API-KEY-ID") String apiKeyId,
            @RequestHeader("X-NCP-APIGW-API-KEY") String apiKey,
            @RequestParam("coords") String coords,
            @RequestParam("orders") String orders,
            @RequestParam("output") String output
    );
}
