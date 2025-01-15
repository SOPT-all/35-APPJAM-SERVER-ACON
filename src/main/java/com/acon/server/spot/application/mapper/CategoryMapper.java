package com.acon.server.spot.application.mapper;

import com.acon.server.spot.domain.entity.Category;
import com.acon.server.spot.infra.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CategoryMapper {

    // CategoryEntity -> Category
    Category toDomain(CategoryEntity entity);

    // Category -> CategoryEntity
    CategoryEntity toEntity(Category domain);
}
