package com.acon.server.spot.application.service;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.global.external.GeoCodingResponse;
import com.acon.server.global.external.NaverMapsAdapter;
import com.acon.server.member.infra.repository.GuidedSpotRepository;
import com.acon.server.spot.api.response.MenuListResponse;
import com.acon.server.spot.api.response.MenuResponse;
import com.acon.server.spot.api.response.SearchSpotListResponse;
import com.acon.server.spot.api.response.SearchSpotResponse;
import com.acon.server.spot.api.response.SearchSuggestionListResponse;
import com.acon.server.spot.api.response.SearchSuggestionResponse;
import com.acon.server.spot.api.response.SpotDetailResponse;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotService {

    private static final int WALKING_RADIUS_30_MIN = 2000;
    private static final int SUGGESTION_LIMIT = 5;
    private static final int VERIFICATION_DISTANCE = 250;

    private final SpotRepository spotRepository;
    private final MenuRepository menuRepository;
    private final OpeningHourRepository openingHourRepository;
    private final SpotImageRepository spotImageRepository;
    private final GuidedSpotRepository guidedSpotRepository;

    private final SpotDtoMapper spotDtoMapper;
    private final SpotMapper spotMapper;

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

        List<SpotEntity> updatedEntityList = spotEntityList.stream()
                .map(spotEntity -> {
                    Spot spot = spotMapper.toDomain(spotEntity);
                    updateSpotCoordinate(spot);
                    return spotMapper.toEntity(spot);
                })
                .toList();
        spotRepository.saveAll(updatedEntityList);

        log.info("위도 또는 경도 정보가 비어 있는 Spot 데이터 {}건을 업데이트 했습니다.", updatedEntityList.size());
    }

    // 메서드 설명: spotId에 해당하는 Spot의 좌표를 업데이트한다. (주소 -> 좌표)
    private void updateSpotCoordinate(final Spot spot) {
        GeoCodingResponse geoCodingResponse = naverMapsAdapter.getGeoCodingResult(spot.getAddress());

        spot.updateCoordinate(
                Double.parseDouble(geoCodingResponse.latitude()),
                Double.parseDouble(geoCodingResponse.longitude())
        );
        spot.updateLocation();
    }

    // TODO: 장소 추천 시 메뉴 가격 변동이면 메인 메뉴 X 처리

    // TODO: 트랜잭션 범위 고민하기
    // 메서드 설명: spotId에 해당하는 Spot의 상세 정보를 조회한다. (메뉴, 이미지, 영업 여부 등)
    @Transactional
    public SpotDetailResponse fetchSpotDetail(final Long spotId) {
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

        return spotDtoMapper.toSpotDetailResponse(spotEntity, imageList, isSpotOpen(spotId));
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
        if (!spotRepository.existsById(spotId)) {
            throw new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR);
        }

        List<MenuEntity> menuEntityList = menuRepository.findAllBySpotId(spotId);

        // TODO: mapper로 변경
        List<MenuResponse> menuResponseList = menuEntityList.stream()
                .map(menu -> MenuResponse.builder()
                        .id(menu.getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .image(menu.getImage())
                        .build())
                .toList();

        return new MenuListResponse(menuResponseList);
    }

    @Transactional(readOnly = true)
    public SearchSuggestionListResponse fetchSearchSuggestions(final Double latitude, final Double longitude) {
        // TODO: 토큰 검증 이후 MemberID 추출 로직 필요
        List<SearchSuggestionResponse> recentSpotSuggestion =
                guidedSpotRepository.findTopByMemberIdOrderByUpdatedAtDesc(1L)
                        .flatMap(recentGuidedSpot -> spotRepository.findById(recentGuidedSpot.getSpotId()))
                        .map(spotDtoMapper::toSearchSuggestionResponse)
                        .stream()
                        .toList();

        List<SearchSuggestionResponse> nearestSpotList =
                findNearestSpotList(longitude, latitude, WALKING_RADIUS_30_MIN, SUGGESTION_LIMIT);

        // Set을 통한 필터링 성능 향상
        Set<Long> recentSpotIds = recentSpotSuggestion.stream()
                .map(SearchSuggestionResponse::spotId)
                .collect(Collectors.toSet());

        List<SearchSuggestionResponse> filteredNearestSpotList = nearestSpotList.stream()
                .filter(nearestSpot -> !recentSpotIds.contains(nearestSpot.spotId()))
                .toList();

        List<SearchSuggestionResponse> combinedSuggestionList = Stream.concat(
                        recentSpotSuggestion.stream(),
                        filteredNearestSpotList.stream()
                )
                .limit(SUGGESTION_LIMIT)
                .toList();

        return new SearchSuggestionListResponse(combinedSuggestionList);
    }

    public List<SearchSuggestionResponse> findNearestSpotList(
            double longitude,
            double latitude,
            double radius,
            int limit) {
        List<Object[]> rawFindResults = spotRepository.findNearestSpots(longitude, latitude, radius, limit);

        return rawFindResults.stream()
                .map(result -> new SearchSuggestionResponse((Long) result[0], (String) result[1]))
                .toList();
    }

    public SearchSpotListResponse searchSpot(final String keyword) {
        List<SpotEntity> spotEntityList = spotRepository.findTop10ByNameContainsIgnoreCase(keyword);

        // TODO: mapper로 변경
        List<SearchSpotResponse> spotList = spotEntityList.stream()
                .map(spotEntity -> SearchSpotResponse.builder()
                        .spotId(spotEntity.getId())
                        .name(spotEntity.getName())
                        .address(spotEntity.getAddress())
                        .spotType(spotEntity.getSpotType())
                        .build())
                .toList();

        return new SearchSpotListResponse(spotList);
    }

    @Transactional(readOnly = true)
    public boolean verifySpot(Long spotId, Double memberLongitude, Double memberLatitude) {
        SpotEntity spotEntity = spotRepository.findByIdOrThrow(spotId);
        Double spotLongitude = spotEntity.getLongitude();
        Double spotLatitude = spotEntity.getLatitude();
        Double distance =
                spotRepository.calculateDistance(spotLongitude, spotLatitude, memberLongitude, memberLatitude);

        return distance < VERIFICATION_DISTANCE;
    }
}
