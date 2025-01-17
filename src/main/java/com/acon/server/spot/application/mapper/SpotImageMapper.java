package com.acon.server.spot.application.mapper;

import com.acon.server.spot.domain.entity.SpotImage;
import com.acon.server.spot.infra.entity.SpotImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SpotImageMapper {

    // SpotImageEntity -> SpotImage
    SpotImage toDomain(SpotImageEntity entity);

    // SpotImage -> SpotImageEntity
    SpotImageEntity toEntity(SpotImage domain);
}
