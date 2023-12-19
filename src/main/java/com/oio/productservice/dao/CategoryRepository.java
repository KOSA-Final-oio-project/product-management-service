package com.oio.productservice.dao;

import com.oio.productservice.jpa.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query(nativeQuery=true,
            value = "SELECT category_name FROM category")
    List<String> listOfCategory();

    CategoryEntity findByCategoryName(String categoryName);
}
