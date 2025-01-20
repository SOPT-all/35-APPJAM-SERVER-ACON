package com.acon.server.global.external;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverMapsAdapter {

    private final NaverMapsClient naverMapsClient;

    public GeoCodingResponse getGeoCodingResult(String address) {
        // TODO: try-catch로 감싸기
        return Optional.ofNullable(naverMapsClient.getGeoCode(address))
                .map(response -> (List<Map<String, Object>>) response.get("addresses"))
                .filter(addresses -> !addresses.isEmpty())
                .map(addresses -> addresses.get(0))
                .map(firstAddress -> new GeoCodingResponse(
                        (String) firstAddress.get("x"),
                        (String) firstAddress.get("y")
                ))
                .orElseThrow(
                        () -> new BusinessException(ErrorType.NAVER_MAPS_GEOCODING_API_ERROR));
    }
}
