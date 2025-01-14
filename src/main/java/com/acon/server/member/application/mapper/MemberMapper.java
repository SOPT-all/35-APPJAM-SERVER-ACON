package com.acon.server.member.application.mapper;

import com.acon.server.member.domain.entity.Member;
import com.acon.server.member.infra.entity.member.MemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MemberMapper {

    // MemberEntity -> Member
    Member toDomain(MemberEntity entity);

    // Member -> MemberEntity
    MemberEntity toEntity(Member domain);
}
