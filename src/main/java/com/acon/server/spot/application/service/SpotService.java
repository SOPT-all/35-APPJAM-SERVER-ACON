package com.acon.server.spot.application.service;

import com.acon.server.global.auth.PrincipalHandler;
import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.global.external.GeoCodingResponse;
import com.acon.server.global.external.NaverMapsAdapter;
import com.acon.server.member.infra.entity.MemberEntity;
import com.acon.server.member.infra.repository.GuidedSpotRepository;
import com.acon.server.member.infra.repository.MemberRepository;
import com.acon.server.spot.api.request.SpotListRequest;
import com.acon.server.spot.api.response.MenuListResponse;
import com.acon.server.spot.api.response.MenuResponse;
import com.acon.server.spot.api.response.SearchSpotListResponse;
import com.acon.server.spot.api.response.SearchSpotResponse;
import com.acon.server.spot.api.response.SearchSuggestionListResponse;
import com.acon.server.spot.api.response.SearchSuggestionResponse;
import com.acon.server.spot.api.response.SpotDetailResponse;
import com.acon.server.spot.api.response.SpotListResponse;
import com.acon.server.spot.api.response.SpotListResponse.RecommendedSpot;
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
import com.acon.server.spot.infra.repository.SpotNativeQueryRepository;
import com.acon.server.spot.infra.repository.SpotRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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

    // TODO: 매직 넘버 yml로 옮기기, 250m로 바꾸고 변수명 수정
    private static final int WALKING_RADIUS_30_MIN = 2000;
    private static final int SUGGESTION_LIMIT = 5;
    private static final int VERIFICATION_DISTANCE = 250;
    private static final int SEARCH_LIMIT = 10;

    private final GuidedSpotRepository guidedSpotRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final OpeningHourRepository openingHourRepository;
    private final SpotImageRepository spotImageRepository;
    private final SpotRepository spotRepository;
    private final SpotNativeQueryRepository spotNativeQueryRepository;

    private final SpotDtoMapper spotDtoMapper;
    private final SpotMapper spotMapper;

    private final PrincipalHandler principalHandler;

    private final NaverMapsAdapter naverMapsAdapter;

    private static final List<String> IMAGE_URLS = List.of(
            "https://github.com/user-attachments/assets/52e88bff-2577-4f0e-ae98-9636f88afd2a",
            "https://github.com/user-attachments/assets/3b0a61cc-7b96-45c1-ab4f-0b1b38597f4b",
            "https://github.com/user-attachments/assets/6e4a4cad-7467-4662-83e7-3ada27c1e6e6",
            "https://github.com/user-attachments/assets/5ce1fdc3-178f-4d54-bb00-4db97afa6b93",
            "https://github.com/user-attachments/assets/870990b0-b414-496d-99b9-744a85ac4c9c",
            "https://github.com/user-attachments/assets/52e15f07-4770-4f35-a6e5-65cedc737251"
    );
    private static final Random RANDOM = new Random();

    // 메서드 설명: 위치 정보가 없는 Spot들의 위치 정보를 업데이트한다.
    @Transactional
    public void updateNullCoordinatesForSpots() {
        List<SpotEntity> spotEntityList = spotRepository.findAllByLatitudeIsNullOrLongitudeIsNullOrGeomIsNullOrLegalDongIsNull();

        if (spotEntityList.isEmpty()) {
            log.info("위치 정보가 비어 있는 Spot 데이터가 없습니다.");
            return;
        }

        log.info("위치 정보가 비어 있는 Spot 데이터를 {}건 찾았습니다.", spotEntityList.size());

        List<SpotEntity> updatedEntityList = spotEntityList.stream()
                .map(spotEntity -> {
                    Spot spot = spotMapper.toDomain(spotEntity);
                    updateSpotCoordinate(spot);
                    return spotMapper.toEntity(spot);
                })
                .toList();
        spotRepository.saveAll(updatedEntityList);

        log.info("위치 정보가 비어 있는 Spot 데이터 {}건을 업데이트 했습니다.", updatedEntityList.size());
    }

    // TODO: 로직 정리
    // 메서드 설명: 도로명 주소를 바탕으로 spotId에 해당하는 Spot의 위치 정보를 업데이트한다.
    private void updateSpotCoordinate(final Spot spot) {
        GeoCodingResponse geoCodingResponse = naverMapsAdapter.getGeoCodingResult(spot.getAddress());

        spot.updateCoordinate(
                Double.parseDouble(geoCodingResponse.latitude()),
                Double.parseDouble(geoCodingResponse.longitude())
        );
        spot.updateGeom();
        spot.updateLegalDong(
                naverMapsAdapter.getReverseGeoCodingResult(spot.getLatitude(), spot.getLongitude())
        );
    }

    @Transactional(readOnly = true)
    public SpotListResponse fetchRecommendedSpotList(SpotListRequest request) {
        List<SpotEntity> randomSpots = spotRepository.findRandomSpots(6);

        List<SpotEntity> filterSpotList = filterSpotList(request);

        // Spot 도메인 엔티티를 SpotListResponse.RecommendedSpot으로 변환
        List<RecommendedSpot> spotList = filterSpotList.stream()
                .map(this::toRecommendedSpot)
                .collect(Collectors.toList());

        return new SpotListResponse(spotList);
    }

    // TODO enum 처리 급해요 
    public List<SpotEntity> filterSpotList(SpotListRequest request) {
        return spotNativeQueryRepository.findSpotsWithinDistance(
                request.latitude(), request.longitude(),
                calculateDistanceFromWalkingTime(request.condition().walkingTime()),
                request.condition().spotType(),
                request.condition().priceRange(),
                request.condition()
                        .filterList(), 6
        );
    }

    // Spot -> RecommendedSpot 변환 메서드
    private RecommendedSpot toRecommendedSpot(SpotEntity spotEntity) {
        return new RecommendedSpot(
                spotEntity.getId(),
                fetchSpotImage(spotEntity.getId()),
                generateRandomMatchingRate(), // 80~100 사이의 랜덤 값 설정
                spotEntity.getSpotType().name(),
                spotEntity.getName(),
                calculateWalkingTime(spotEntity.getLatitude(), spotEntity.getLongitude())
        );
    }

    private String fetchSpotImage(Long spotId) {
        return spotImageRepository.findTopBySpotId(spotId)
                .map(SpotImageEntity::getImage)
                .orElse(IMAGE_URLS.get(RANDOM.nextInt(IMAGE_URLS.size())));
    }

    // 80~100 사이 랜덤값 생성
    private int generateRandomMatchingRate() {
        return (int) (Math.random() * (100 - 80 + 1)) + 80; // 80~100 사이 값
    }

    // 예시: 도보 시간 계산 (임의로 설정)
    private int calculateWalkingTime(Double latitude, Double longitude) {
        // 도보 시간 계산 로직 (예시로 3~10분 사이 랜덤 값 반환)
        return (int) (Math.random() * (10 - 3 + 1)) + 3;
    }

    // TODO: 장소 추천 시 메뉴 가격 변동이면 메인 메뉴 X 처리

    // TODO: 트랜잭션 범위 고민하기
    // 메서드 설명: spotId에 해당하는 Spot의 상세 정보를 조회한다. (메뉴, 이미지, 영업 여부 등)
    @Transactional
    public SpotDetailResponse fetchSpotDetail(final Long spotId) {
        SpotEntity spotEntity = spotRepository.findByIdOrElseThrow(spotId);
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
        MemberEntity memberEntity = memberRepository.findByIdOrElseThrow(principalHandler.getUserIdFromPrincipal());

        List<SearchSuggestionResponse> recentSpotSuggestion =
                // TODO: 250m 범위 내의 TOP5로 수정
                guidedSpotRepository.findTopByMemberIdOrderByUpdatedAtDesc(memberEntity.getId())
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

    // TODO: limit 없는 메서드로부터 분기하도록 리팩토링
    private List<SearchSuggestionResponse> findNearestSpotList(
            double longitude,
            double latitude,
            double radius,
            int limit
    ) {
        List<Object[]> rawFindResults = spotRepository.findNearestSpotList(longitude, latitude, radius, limit);

        return rawFindResults.stream()
                .map(result -> new SearchSuggestionResponse((Long) result[0], (String) result[1]))
                .toList();
    }

    public SearchSpotListResponse searchSpot(final String keyword) {
        System.out.println(keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return new SearchSpotListResponse(Collections.emptyList());
        }
        List<SpotEntity> spotEntityList = spotRepository.findTop10ByNameStartingWith(keyword);

        if (spotEntityList.size() < SEARCH_LIMIT) {
            List<SpotEntity> additionalSpots = spotRepository.findByNameContainingWithLimit(keyword,
                    SEARCH_LIMIT - spotEntityList.size());
            Set<Long> existingSpotIds = spotEntityList.stream()
                    .map(SpotEntity::getId)
                    .collect(Collectors.toSet());
            spotEntityList.addAll(
                    additionalSpots.stream()
                            .filter(additionalSpot -> !existingSpotIds.contains(additionalSpot.getId()))
                            .toList()
            );
        }

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
        if (!spotRepository.existsById(spotId)) {
            throw new BusinessException(ErrorType.NOT_FOUND_SPOT_ERROR);
        }

        Double distance =
                spotRepository.calculateDistanceFromSpot(spotId, memberLongitude, memberLatitude);

        return distance < VERIFICATION_DISTANCE;
    }

    private double calculateDistanceFromWalkingTime(int walkingTime) {
        double distanceKm = (walkingTime / 60.0) * 4.0;
        return distanceKm * 1000.0;
    }
}
