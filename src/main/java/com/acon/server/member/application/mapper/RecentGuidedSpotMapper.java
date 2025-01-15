package com.acon.server.member.application.mapper;

import com.acon.server.member.domain.entity.RecentGuidedSpot;
import com.acon.server.member.infra.entity.RecentGuidedSpotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RecentGuidedSpotMapper {

    // RecentGuidedSpotEntity -> RecentGuidedSpot
    RecentGuidedSpot toDomain(RecentGuidedSpotEntity entity);

    // RecentGuidedSpot -> RecentGuidedSpotEntity
    RecentGuidedSpotEntity toEntity(RecentGuidedSpot domain);
}
