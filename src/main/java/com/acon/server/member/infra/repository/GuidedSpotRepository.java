package com.acon.server.member.infra.repository;

import com.acon.server.member.infra.entity.GuidedSpotEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuidedSpotRepository extends JpaRepository<GuidedSpotEntity, Long> {

    Optional<GuidedSpotEntity> findTopByMemberIdOrderByUpdatedAtDesc(Long memberId);
}
