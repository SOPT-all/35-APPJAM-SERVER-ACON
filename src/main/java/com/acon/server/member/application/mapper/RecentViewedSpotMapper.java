package com.acon.server.member.application.mapper;

import com.acon.server.member.domain.entity.RecentViewedSpot;
import com.acon.server.member.infra.entity.member.RecentViewedSpotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RecentViewedSpotMapper {

    // RecentViewedSpotEntity -> RecentViewedSpot
    RecentViewedSpot toDomain(RecentViewedSpotEntity entity);

    // RecentViewedSpot -> RecentViewedSpotEntity
    RecentViewedSpotEntity toEntity(RecentViewedSpot domain);
}
