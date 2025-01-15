package com.acon.server.member.application.mapper;

import com.acon.server.member.domain.entity.VerifiedArea;
import com.acon.server.member.infra.entity.VerifiedAreaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VerifiedAreaMapper {

    // VerifiedAreaEntity -> VerifiedArea
    VerifiedArea toDomain(VerifiedAreaEntity entity);

    // VerifiedArea -> VerifiedAreaEntity
    VerifiedAreaEntity toEntity(VerifiedArea domain);
}
