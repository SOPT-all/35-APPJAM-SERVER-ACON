package com.acon.server.review.application.mapper;

import com.acon.server.member.infra.entity.review.ReviewImageEntity;
import com.acon.server.review.domain.entity.ReviewImage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReviewImageMapper {

    // ReviewImageEntity -> ReviewImage
    ReviewImage toDomain(ReviewImageEntity entity);

    // ReviewImage -> ReviewImageEntity
    ReviewImageEntity toEntity(ReviewImage domain);
}
