package com.acon.server.spot.application.mapper;

import com.acon.server.spot.domain.entity.Menu;
import com.acon.server.spot.infra.entity.MenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MenuMapper {

    // MenuEntity -> Menu
    Menu toDomain(MenuEntity entity);

    // Menu -> MenuEntity
    MenuEntity toEntity(Menu domain);
}
