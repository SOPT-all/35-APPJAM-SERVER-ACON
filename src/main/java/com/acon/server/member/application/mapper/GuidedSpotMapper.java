package com.acon.server.member.application.mapper;

import com.acon.server.member.domain.entity.GuidedSpot;
import com.acon.server.member.infra.entity.GuidedSpotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GuidedSpotMapper {

    // GuidedSpotEntity -> GuidedSpot
    GuidedSpot toDomain(GuidedSpotEntity entity);

    // GuidedSpot -> GuidedSpotEntity
    GuidedSpotEntity toEntity(GuidedSpot domain);
}
