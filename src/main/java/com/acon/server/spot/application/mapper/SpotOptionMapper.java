package com.acon.server.spot.application.mapper;

import com.acon.server.spot.domain.entity.SpotOption;
import com.acon.server.spot.infra.entity.SpotOptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SpotOptionMapper {

    // SpotOptionEntity -> SpotOption
    SpotOption toDomain(SpotOptionEntity entity);

    // SpotOption -> SpotOptionEntity
    SpotOptionEntity toEntity(SpotOption domain);
}
