package com.oio.productservice.dao;

import com.oio.productservice.jpa.ProductEntity;
import com.oio.productservice.jpa.QProductEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.*;
import java.util.stream.Collectors;


public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {
    public ProductSearchImpl() {
        super(ProductEntity.class);
    }

    @Override
    public Map<String, Object> findProduct(String types,
                                           String searchWord,
                                           String siDo, String siGunGu, String eupMyeonRo,
                                           String categoryName) {
        QProductEntity product = QProductEntity.productEntity;
        JPQLQuery<ProductEntity> query = from(product);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (searchWord != null) {
            booleanBuilder.and(product.title.contains(searchWord));
        }

        if (siDo != null) {
            booleanBuilder.and(product.address.siDo.eq(siDo));

            if (siGunGu != null) {
                booleanBuilder.and(product.address.siGunGu.eq(siGunGu));

                if (eupMyeonRo != null) {
                    booleanBuilder.and(product.address.eupMyeonRo.eq(eupMyeonRo));
                }
            }
        }

        if (categoryName != null) {
            booleanBuilder.and(product.category.categoryName.eq(categoryName));
        }

        List<ProductEntity> productEntityList = null;

        switch (types) {
            case "n":
                productEntityList = query
                        .where(booleanBuilder)
                        .orderBy(product.postDate.desc())
                        .fetch();
                break;
            case "v":
            case "r":
                productEntityList = query
                        .where(booleanBuilder)
                        .fetch();

                // Iterator to remove elements with status 2
                Iterator<ProductEntity> iterator = productEntityList.iterator();
                while (iterator.hasNext()) {
                    ProductEntity productEntity = iterator.next();
                    if (productEntity.getStatus() == 2) {
                        iterator.remove();
                    }
                }

                // Sorting after removal
                if ("v".equals(types)) {
                    productEntityList.sort(Comparator.comparing(ProductEntity::getViewCount).reversed());
                } else if ("r".equals(types)) {
                    productEntityList.sort(Comparator.comparing(ProductEntity::getRentCount).reversed());
                }

                // Limiting to 3 results
                productEntityList = productEntityList.stream().limit(3).collect(Collectors.toList());
                break;
        }

        double averagePrice = 0;

        if(searchWord != null && productEntityList != null && !productEntityList.isEmpty()) {
            Long totalPrice = productEntityList.stream()
                    .mapToLong(ProductEntity::getPrice)
                    .sum();

            long productCount = productEntityList.size();

            averagePrice = totalPrice / productCount;
        }

        Map<String, Object> result =  new HashMap<>();
        result.put("productList", productEntityList);
        result.put("avgPrice", averagePrice);


        return result;
    }

    @Override
    public Long calculateAveragePrice(List<ProductEntity> productList) {

        Long totalPrice = productList.stream()
                .mapToLong(ProductEntity::getPrice)
                .sum();

        long productCount = productList.size();

        // 평균 가격을 계산
        long averagePrice = totalPrice / productCount;

        return averagePrice;
    }


}
