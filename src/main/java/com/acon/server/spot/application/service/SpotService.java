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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotService {

    private final SpotRepository spotRepository;
    private final MenuRepository menuRepository;
    private final SpotImageRepository spotImageRepository;
    private final OpeningHourRepository openingHourRepository;

    private final SpotMapper spotMapper;
    private final SpotDtoMapper spotDtoMapper;

    private final NaverMapsAdapter naverMapsAdapter;

    // 메서드 설명: 위도, 경도 정보가 없는 Spot들의 좌표를 업데이트한다.
    @Transactional
    public void updateNullCoordinatesForSpots() {
        List<SpotEntity> spotEntityList = spotRepository.findAllByLatitudeIsNullOrLongitudeIsNull();

        if (spotEntityList.isEmpty()) {
            log.info("위도 또는 경도 정보가 비어 있는 Spot 데이터가 없습니다.");
            return;
        }

        log.info("위도 또는 경도 정보가 비어 있는 Spot 데이터를 {}건 찾았습니다.", spotEntityList.size());

        List<SpotEntity> updatedEntities = spotEntityList.stream()
                .map(spotEntity -> {
                    Spot spot = spotMapper.toDomain(spotEntity);
                    updateSpotCoordinate(spot);
                    return spotMapper.toEntity(spot);
                })
                .toList();
        spotRepository.saveAll(updatedEntities);

        log.info("위도 또는 경도 정보가 비어 있는 Spot 데이터 {}건을 업데이트 했습니다.", updatedEntities.size());
    }

    // 메서드 설명: spotId에 해당하는 Spot의 좌표를 업데이트한다. (주소 -> 좌표)
    private void updateSpotCoordinate(final Spot spot) {
        GeoCodingResponse geoCodingResponse = naverMapsAdapter.getGeoCodingResult(spot.getAddress());

        spot.updateCoordinate(
                Double.parseDouble(geoCodingResponse.latitude()),
                Double.parseDouble(geoCodingResponse.longitude())
        );
    }

    // TODO: 트랜잭션 범위 고민하기
    // 메서드 설명: spotId에 해당하는 Spot의 상세 정보를 조회한다. (메뉴, 이미지, 영업 여부 등)
    @Transactional
    public MenuDetailResponse fetchSpotDetail(final Long spotId) {
        SpotEntity spotEntity = spotRepository.findByIdOrThrow(spotId);
        Spot spot = spotMapper.toDomain(spotEntity);

        List<SpotImageEntity> spotImageEntityList = spotImageRepository.findAllBySpotId(spotId);
        List<String> imageList = spotImageEntityList.stream()
                .map(SpotImageEntity::getImage)
                .toList();

        if (spot.getLatitude() == null || spot.getLongitude() == null) {
            updateSpotCoordinate(spot);
            spotEntity = spotRepository.save(spotMapper.toEntity(spot));
        }

        return spotDtoMapper.toMenuDetailResponse(spotEntity, imageList, isSpotOpen(spotId));
    }

    // 메서드 설명: spotId에 해당하는 Spot이 현재 영업 중인지 확인한다. (영업 시간에 속하는지)
    private boolean isSpotOpen(final Long spotId) {
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

    // 메서드 설명: currentTime이 영업 시간에 속하는지 확인한다. (자정이 지난 후)
    private boolean isAfterMidnight(final LocalTime currentTime, final OpeningHourEntity openingHour) {
        LocalTime startTime = openingHour.getStartTime();
        LocalTime endTime = openingHour.getEndTime();

        return endTime.isBefore(startTime) && currentTime.isAfter(LocalTime.MIDNIGHT) && currentTime.isBefore(endTime);
    }

    // 메서드 설명: currentTime이 영업 시간에 속하는지 확인한다. (자정이 지나기 전)
    private boolean isBeforeMidnight(final LocalTime currentTime, final OpeningHourEntity openingHour) {
        LocalTime startTime = openingHour.getStartTime();
        LocalTime endTime = openingHour.getEndTime();

        if (endTime.isBefore(startTime)) {
            return currentTime.isAfter(startTime) && currentTime.isBefore(LocalTime.MIDNIGHT);
        }

        return currentTime.isAfter(startTime) && currentTime.isBefore(endTime);
    }

    @Transactional(readOnly = true)
    public MenuListResponse fetchMenus(final Long spotId) {
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
