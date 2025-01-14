package com.acon.server.member.infra.repository;

import com.acon.server.member.domain.enums.SocialType;
import com.acon.server.member.infra.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

}
