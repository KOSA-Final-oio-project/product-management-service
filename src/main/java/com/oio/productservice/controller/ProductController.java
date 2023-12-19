package com.oio.productservice.controller;

import com.oio.productservice.dto.Product;
import com.oio.productservice.dto.Request;
import com.oio.productservice.jpa.ProductEntity;
import com.oio.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("/product")
@RestController
public class ProductController {
    @Autowired
    private ProductService ps;

    @PostMapping(value = "/writeProduct/{siDo}/{siGunGu}/{eupMyeonRo}/{categoryName}/{nickname}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> writeProduct(@PathVariable String categoryName,
                                          @PathVariable String siDo, @PathVariable String siGunGu, @PathVariable String eupMyeonRo,
                                          @PathVariable String nickname,
                                          ProductEntity product,
                                          List<MultipartFile> files) throws IOException {
//        LocalDate todayDate = LocalDate.now();

        // Date를 Instant로 변환 후 Instant를 LocalDate로 변환
//        LocalDate startDate = product.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate endDate = product.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

//        if (startDate.isBefore(todayDate)) {
//            return ResponseEntity.badRequest().body("시작일자는 오늘보다 이전으로 지정할 수 없습니다");
//        } else if (endDate.isBefore(todayDate)) {
//            return ResponseEntity.badRequest().body("종료일자는 오늘보다 이전으로 저장할 수 없습니다");
//        } else if (endDate.isBefore(startDate)) {
//            return ResponseEntity.badRequest().body("종료일자는 시작일자보다 이전으로 지정할 수 없습니다");
//        } else {
            ps.insertProduct(categoryName, siDo, siGunGu, eupMyeonRo, nickname, product, files);
            return new ResponseEntity<>(HttpStatus.OK);
        }


    //이미지수정X
    @PutMapping("/modifyProduct/{productNo}")
    public ResponseEntity<?> modifyProduct(@PathVariable Long productNo,
                                           @RequestBody ProductEntity product) {
//        LocalDate todayDate = LocalDate.now();

        // Date를 Instant로 변환 후 Instant를 LocalDate로 변환
//        LocalDate endDate = product.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//        if (endDate.isBefore(todayDate)) {
//            return ResponseEntity.badRequest().body("종료일자는 오늘보다 이전으로 지정할 수 없습니다");
//        } else {
            product.setProductNo(productNo);
            ps.updateProduct(product);
            return new ResponseEntity<>(HttpStatus.OK);
        }


//    //이미지수정
//    @PutMapping(value = "/modifyProduct/{productNo}", consumes = "multipart/form-data")
//    public ResponseEntity<?> modifyProduct(@PathVariable Long productNo,
//                                           @RequestPart ProductEntity product,
//                                           @RequestPart List<MultipartFile> files) throws IOException {
//        LocalDate todayDate = LocalDate.now();
//
//        // Date를 Instant로 변환 후 Instant를 LocalDate로 변환
//        LocalDate endDate = product.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//        if (endDate.isBefore(todayDate)) {
//            return ResponseEntity.badRequest().body("종료일자는 오늘보다 이전으로 지정할 수 없습니다");
//        } else {
//            product.setProductNo(productNo);
//            ps.updateProduct(product, files);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//    }

    @GetMapping(value = "/productDetail/{productNo}/{nickname}", produces = "application/json;charset=UTF-8")
    public Map<String, Object> productDetail(@PathVariable Long productNo, @PathVariable String nickname) {
        return ps.selectProductAndUpdateViewCount(productNo, nickname);
    }

    @GetMapping("/productList/{types}")
    public Map productList(@PathVariable String types, Request request) {
        //types "n":일반,"v":조회수top3,"r":대여수top3
        return ps.productList(types, request);
    }

    @GetMapping(value = "myProduct/{nickname}/{postCategory}", produces = "application/json;charset=UTF-8")
    public List<Product> myProduct(@PathVariable String nickname, @PathVariable Integer postCategory) {
        return ps.myProduct(nickname, postCategory);
    }

    @DeleteMapping("/delete/{productNo}")
    public void deleteProduct(@PathVariable Long productNo) {
        ps.deleteProductById(productNo);
    }

//    @PutMapping("/test/{productNo}")
//    public ResponseEntity<?> test(@PathVariable Long productNo) {
//        ps.changeStatusRented(productNo);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

//    @DeleteMapping(value = "test/{nickname}", produces = "application/json;charset=UTF-8")
//    public void test(@PathVariable String nickname) {
//        ps.deleteProductByNickname(nickname);
//    }

}
