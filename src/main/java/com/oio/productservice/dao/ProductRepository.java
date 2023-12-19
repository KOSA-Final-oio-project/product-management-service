package com.oio.productservice.dao;

import com.oio.productservice.jpa.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Long>, ProductSearch {
    @Procedure(name = ProductEntity.NamedQuery_IncreaseViewCount)
    ProductEntity procedure(@Param("productNo") Long productNo);

    @Query(nativeQuery = true,
            value = "SELECT * FROM product WHERE TRUNC(end_date) = :endDate")
    List<ProductEntity> findByEndDate(Date endDate);


    List<ProductEntity> findByNicknameAndPostCategory(String nickname, Integer postCategory);

    void deleteByNickname(String nickname);

//    @Query(nativeQuery = true,
//            value = "UPDATE product SET status=1 WHERE product_no = :productNo")
//    void updateStatusTo1(@Param("productNo")Long productNo);
//
//    @Query(nativeQuery = true,
//            value = "UPDATE product SET status=2 WHERE TRUNC(end_date) = :endDate")
//    void updateStatusTo2(@Param("endDate") LocalDate endDate);
}