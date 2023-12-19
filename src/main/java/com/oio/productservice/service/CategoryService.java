package com.oio.productservice.service;

import com.oio.productservice.dao.CategoryRepository;
import com.oio.productservice.jpa.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository cr;

    public List<String> listOfCategory() {
        return cr.listOfCategory();
    }

    public void insertCategory(CategoryEntity category){
        CategoryEntity c = CategoryEntity.builder()
                                         .categoryName(category.getCategoryName())
                                         .build();
        cr.save(c);
    }


}
