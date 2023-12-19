package com.oio.productservice.dao;

import com.oio.productservice.jpa.ProductEntity;

import java.util.List;
import java.util.Map;

public interface ProductSearch {
    Map<String, Object> findProduct(String types,
                                    String searchWord,
                                    String siDo, String siGunGu, String eupMyeonRo,
                                    String categoryName);

    Long calculateAveragePrice(List<ProductEntity> productList);
}
