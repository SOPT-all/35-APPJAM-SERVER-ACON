package com.acon.server.member.infra.repository;

import com.acon.server.member.infra.entity.VerifiedAreaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifiedAreaRepository extends JpaRepository<VerifiedAreaEntity, Long> {

    boolean existsByMemberIdAndName(Long memberId, String name);

    Optional<VerifiedAreaEntity> findByMemberIdAndName(Long memberId, String name);

    List<VerifiedAreaEntity> findAllByMemberId(Long memberId);
}