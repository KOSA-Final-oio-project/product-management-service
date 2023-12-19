package com.oio.productservice.controller;

import com.oio.productservice.jpa.CategoryEntity;
import com.oio.productservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/category")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService cs;

    @GetMapping("/categoryList")
    public List<String> categoryList() {
        return cs.listOfCategory();
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestBody CategoryEntity category) {
        cs.insertCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
