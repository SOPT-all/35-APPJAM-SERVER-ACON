package com.acon.server.member.application.mapper;

import com.acon.server.member.domain.entity.Preference;
import com.acon.server.member.infra.entity.PreferenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PreferenceMapper {

    // PreferenceEntity -> Preference
    Preference toDomain(PreferenceEntity entity);

    // Preference -> PreferenceEntity
    PreferenceEntity toEntity(Preference domain);
}
