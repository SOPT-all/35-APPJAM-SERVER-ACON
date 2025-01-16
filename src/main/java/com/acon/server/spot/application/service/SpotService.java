package com.acon.server.spot.application.service;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.acon.server.spot.api.response.MenuListResponse;
import com.acon.server.spot.api.response.MenuResponse;
import com.acon.server.spot.infra.entity.MenuEntity;
import com.acon.server.spot.infra.repository.MenuRepository;
import com.acon.server.spot.infra.repository.SpotRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final MenuRepository menuRepository;

    public MenuListResponse fetchMenus(Long spotId) {
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
