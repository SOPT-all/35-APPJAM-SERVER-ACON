package com.acon.server.member.infra.repository;

import com.acon.server.member.infra.entity.VerifiedAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifiedAreaRepository extends JpaRepository<VerifiedAreaEntity, Long> {

    boolean existsByMemberIdAndName(Long memberId, String name);
}
