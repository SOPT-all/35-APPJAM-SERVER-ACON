package com.acon.server.member.application.mapper;

import com.acon.server.member.domain.entity.ViewedSpot;
import com.acon.server.member.infra.entity.ViewedSpotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ViewedSpotMapper {

    // ViewedSpotEntity -> ViewedSpot
    ViewedSpot toDomain(ViewedSpotEntity entity);

    // ViewedSpot -> ViewedSpotEntity
    ViewedSpotEntity toEntity(ViewedSpot domain);
}
