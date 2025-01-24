package com.acon.server.member.infra.repository;

import com.acon.server.member.infra.entity.PreferenceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<PreferenceEntity, Long> {

    Optional<PreferenceEntity> findByMemberId(Long memberId);
}
