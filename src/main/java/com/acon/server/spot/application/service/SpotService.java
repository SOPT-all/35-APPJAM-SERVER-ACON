package com.acon.server.spot.application.service;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.global.external.GeoCodingResponse;
import com.acon.server.global.external.NaverMapsAdapter;
import com.acon.server.spot.api.response.MenuDetailResponse;
import com.acon.server.spot.api.response.MenuListResponse;
import com.acon.server.spot.api.response.MenuResponse;
import com.acon.server.spot.application.mapper.SpotDtoMapper;
import com.acon.server.spot.application.mapper.SpotMapper;
import com.acon.server.spot.domain.entity.Spot;
import com.acon.server.spot.infra.entity.MenuEntity;
import com.acon.server.spot.infra.entity.OpeningHourEntity;
import com.acon.server.spot.infra.entity.SpotEntity;
import com.acon.server.spot.infra.entity.SpotImageEntity;
import com.acon.server.spot.infra.repository.MenuRepository;
import com.acon.server.spot.infra.repository.OpeningHourRepository;
import com.acon.server.spot.infra.repository.SpotImageRepository;
import com.acon.server.spot.infra.repository.SpotRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final MenuRepository menuRepository;
    private final SpotImageRepository spotImageRepository;
    private final OpeningHourRepository openingHourRepository;

    private final SpotMapper spotMapper;
    private final SpotDtoMapper spotDtoMapper;

    private final NaverMapsAdapter naverMapsAdapter;

    // TODO: 트랜잭션 범위 고민하기
    @Transactional
    public MenuDetailResponse fetchSpotDetail(final Long spotId) {
        SpotEntity spotEntity = spotRepository.findByIdOrThrow(spotId);
        Spot spot = spotMapper.toDomain(spotEntity);

        List<SpotImageEntity> spotImageEntityList = spotImageRepository.findAllBySpotId(spotId);
        List<String> imageList = spotImageEntityList.stream()
                .map(SpotImageEntity::getImage)
                .toList();

        if (spot.getLatitude() == null || spot.getLongitude() == null) {
            updateSpotCoordinates(spot);
            spotEntity = spotRepository.save(spotMapper.toEntity(spot));
        }

        return spotDtoMapper.toMenuDetailResponse(spotEntity, imageList, isSpotOpen(spotId));
    }

    private void updateSpotCoordinates(final Spot spot) {
        GeoCodingResponse geoCodingResponse = naverMapsAdapter.getGeoCodingResult(spot.getAddress());

        spot.updateCoordinates(
                Double.parseDouble(geoCodingResponse.latitude()),
                Double.parseDouble(geoCodingResponse.longitude())
        );
    }

    private boolean isSpotOpen(Long spotId) {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();
        DayOfWeek yesterday = today.minus(1);
        LocalTime currentTime = now.toLocalTime();

        // 1. 전날 영업 시간에 속하는지 확인 (자정이 지났을 때)
        List<OpeningHourEntity> yesterdayHours = openingHourRepository.findAllBySpotIdAndDayOfWeek(spotId, yesterday);
        boolean isOpenFromYesterday = yesterdayHours.stream()
                .anyMatch(openingHour -> isAfterMidnight(currentTime, openingHour));

        if (isOpenFromYesterday) {
            return true;
        }

        // 2. 오늘 영업 시간에 속하는지 확인 (자정이 지나기 전)
        List<OpeningHourEntity> todayHours = openingHourRepository.findAllBySpotIdAndDayOfWeek(spotId, today);

        return todayHours.stream()
                .anyMatch(openingHour -> isBeforeMidnight(currentTime, openingHour));
    }

    private boolean isAfterMidnight(LocalTime currentTime, OpeningHourEntity openingHour) {
        LocalTime startTime = openingHour.getStartTime();
        LocalTime endTime = openingHour.getEndTime();

        return endTime.isBefore(startTime) && currentTime.isAfter(LocalTime.MIDNIGHT) && currentTime.isBefore(endTime);
    }

    private boolean isBeforeMidnight(LocalTime currentTime, OpeningHourEntity openingHour) {
        LocalTime startTime = openingHour.getStartTime();
        LocalTime endTime = openingHour.getEndTime();

        if (endTime.isBefore(startTime)) {
            return currentTime.isAfter(startTime) && currentTime.isBefore(LocalTime.MIDNIGHT);
        }
        
        return currentTime.isAfter(startTime) && currentTime.isBefore(endTime);
    }

    @Transactional(readOnly = true)
        if (spotRepository.existsById(spotId)) {
            throw new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR);
        }

        List<MenuEntity> menuEntityList = menuRepository.findAllBySpotId(spotId);

        List<MenuResponse> menuList = menuEntityList.stream()
                .map(menu -> MenuResponse.builder()
                        .id(menu.getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .image(menu.getImage())
                        .build())
                .toList();

        return new MenuListResponse(menuList);
    }
}
