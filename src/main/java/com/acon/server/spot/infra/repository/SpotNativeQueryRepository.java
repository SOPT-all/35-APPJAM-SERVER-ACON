package com.acon.server.spot.infra.repository;

import com.acon.server.spot.api.request.SpotListRequest.Condition.Filter;
import com.acon.server.spot.infra.entity.SpotEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// TODO: 리팩토링
@Repository
public class SpotNativeQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<SpotEntity> findSpotsWithinDistance(
            Double lat,
            Double lng,
            Double distanceMeter,
            String spotType,
            Integer priceRange,
            List<Filter> filterList,
            int limit
    ) {
        // 1) 기본 쿼리
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT s.* ")
                .append("FROM spot s ")
                .append("WHERE ")
                .append("ST_DWithin(s.geom::geography, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography, :distanceMeter) ")
                .append("AND (:spotType IS NULL OR s.spot_type = :spotType) ")
                .append("AND (:priceRange IS NULL OR EXISTS ( ")
                .append("SELECT 1 FROM menu m WHERE m.spot_id = s.id AND m.main_menu = TRUE AND m.price <= :priceRange)) ");

        // 2) filterList가 있는 경우, 각 항목마다 AND EXISTS 서브쿼리 추가
        if (filterList != null && !filterList.isEmpty()) {
            for (int i = 0; i < filterList.size(); i++) {
                sb.append("AND EXISTS (")
                        .append("SELECT 1 FROM spot_option so ")
                        .append("JOIN \"option\" o ON o.id = so.option_id ")
                        .append("JOIN category c ON c.id = o.category_id ")
                        .append("WHERE so.spot_id = s.id ")
                        .append("AND c.name = :categoryName_").append(i).append(" ")
                        .append("AND o.name IN (:optionNames_").append(i).append(") ")
                        .append(") ");
            }
        }

        // 3) LIMIT
        sb.append("LIMIT ").append(limit);

        // 4) Native Query 생성
        Query query = entityManager.createNativeQuery(sb.toString(), SpotEntity.class);

        // 5) 파라미터 바인딩
        query.setParameter("lat", lat);
        query.setParameter("lng", lng);
        query.setParameter("distanceMeter", distanceMeter);
        query.setParameter("spotType", spotType);
        query.setParameter("priceRange", priceRange);

        // filterList 파라미터 바인딩
        if (filterList != null && !filterList.isEmpty()) {
            for (int i = 0; i < filterList.size(); i++) {
                Filter filter = filterList.get(i);
                query.setParameter("categoryName_" + i, filter.category());
                query.setParameter("optionNames_" + i, filter.optionList());
            }
        }

        // 6) 쿼리 실행
        List<SpotEntity> result = query.getResultList();

        return result;
    }
}
