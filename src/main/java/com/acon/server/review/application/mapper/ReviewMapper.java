package com.acon.server.review.application.mapper;

import com.acon.server.review.domain.entity.Review;
import com.acon.server.review.infra.entity.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReviewMapper {

    // ReviewEntity -> Review
    Review toDomain(ReviewEntity entity);

    // Review -> ReviewEntity
    ReviewEntity toEntity(Review domain);
}
