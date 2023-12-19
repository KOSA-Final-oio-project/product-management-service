package com.oio.productservice.dao;

import com.oio.productservice.jpa.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
    @Query(nativeQuery=true,
            value = "SELECT product_image_url FROM product_image WHERE product_no=:productNo")
    List<String> findProductImageUrlByProductNo(Long productNo);

    @Modifying
    @Query(nativeQuery=true,
            value = "DELETE product_image WHERE product_no=:productNo")
    void deleteByProductNo(Long productNo);
}
