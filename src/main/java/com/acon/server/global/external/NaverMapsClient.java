package com.acon.server.global.external;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "naverMapsClient",
        url = "https://naveropenapi.apigw.ntruss.com",
        configuration = NaverMapsFeignConfig.class
)
public interface NaverMapsClient {

    @GetMapping(value = "/map-geocode/v2/geocode")
    Map<String, Object> getGeoCode(
            @RequestParam("query") String query
    );
}
