package com.acon.server.spot.application.mapper;

import com.acon.server.spot.domain.entity.Spot;
import com.acon.server.spot.infra.entity.SpotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SpotMapper {

    // SpotEntity -> Spot
    Spot toDomain(SpotEntity entity);

    // Spot -> SpotEntity
    SpotEntity toEntity(Spot domain);
}
