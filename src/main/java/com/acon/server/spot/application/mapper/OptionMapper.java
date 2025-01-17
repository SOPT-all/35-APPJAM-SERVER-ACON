package com.acon.server.spot.application.mapper;

import com.acon.server.spot.domain.entity.Option;
import com.acon.server.spot.infra.entity.OptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OptionMapper {

    // OptionEntity -> Option
    Option toDomain(OptionEntity entity);

    // Option -> OptionEntity
    OptionEntity toEntity(Option domain);
}
