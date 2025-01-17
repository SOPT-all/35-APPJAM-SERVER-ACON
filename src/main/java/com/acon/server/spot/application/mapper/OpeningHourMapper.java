package com.acon.server.spot.application.mapper;

import com.acon.server.spot.domain.entity.OpeningHour;
import com.acon.server.spot.infra.entity.OpeningHourEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OpeningHourMapper {

    // OpeningHourEntity -> OpeningHour
    OpeningHour toDomain(OpeningHourEntity entity);

    // OpeningHour -> OpeningHourEntity
    OpeningHourEntity toEntity(OpeningHour domain);
}
