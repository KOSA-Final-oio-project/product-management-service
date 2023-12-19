package com.oio.productservice.service;

import com.oio.productservice.dao.AddressRepository;
import com.oio.productservice.dao.CategoryRepository;
import com.oio.productservice.dao.ProductImageRepository;
import com.oio.productservice.dao.ProductRepository;
import com.oio.productservice.dto.Product;
import com.oio.productservice.dto.Request;
import com.oio.productservice.jpa.AddressEntity;
import com.oio.productservice.jpa.CategoryEntity;
import com.oio.productservice.jpa.ProductEntity;
import com.oio.productservice.jpa.ProductImageEntity;
import com.oio.productservice.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository pr;
    @Autowired
    private CategoryRepository cr;
    @Autowired
    private AddressRepository ar;
    @Autowired
    private ProductImageRepository pir;
    @Autowired
    private S3Service s3Service;

    private final ModelMapper modelMapper;

    public void insertProduct(String categoryName, String siDo, String siGunGu, String eupMyeonRo, String nickname, ProductEntity product, List<MultipartFile> files) throws IOException {
        CategoryEntity category = cr.findByCategoryName(categoryName);
        AddressEntity address = ar.findBySiDoAndSiGunGuAndEupMyeonRo(siDo, siGunGu, eupMyeonRo);

        String thumbnailUrl = null;
        if(files != null) {
            MultipartFile first = files.get(0);
            if(first.getSize() > 0) {
                thumbnailUrl = s3Service.upload(files.get(0));
            }
        }


        System.out.println(category);
        System.out.println(address);
        System.out.println(nickname);
        System.out.println(product.getTitle());
        System.out.println(product.getContent());
        System.out.println(product.getPriceCategory());
        System.out.println(product.getPrice());
        System.out.println(product.getStartDate());
        System.out.println(product.getEndDate());
        System.out.println(product.getPostCategory());


        ProductEntity p = ProductEntity.builder()
                .nickname(nickname)
                .category(category)
                .address(address)
                .title(product.getTitle())
                .content(product.getContent())
                .priceCategory(product.getPriceCategory())
                .price(product.getPrice())
                .startDate(product.getStartDate())
                .endDate(product.getEndDate())
                .postCategory(product.getPostCategory())
                .thumbnail(thumbnailUrl)
                .build();
        pr.save(p);

        if(files != null) {
            List<MultipartFile> additionalFiles = files.subList(1, files.size());
            for (MultipartFile additionailFile : additionalFiles) {
                String additionalImageUrl = s3Service.upload(additionailFile);
                ProductImageEntity pi = ProductImageEntity.builder()
                        .productImageUrl(additionalImageUrl)
                        .product(p)
                        .build();
                pir.save(pi);
            }
        }


    }

    public Map<String, Object> selectProductAndUpdateViewCount(Long productNo, String nickname) {
        ProductEntity productEntity = pr.procedure(productNo);

        if (productEntity == null) {
            throw new EntityNotFoundException("Product not found for productNo: " + productNo);
        }
        Product product = modelMapper.map(productEntity, Product.class);

        int status;
        if(Objects.equals(nickname, "관리자")) {
            status = 0; //삭제가능 //관리자
        } else if(Objects.equals(productEntity.getNickname(), nickname)) {
            status = 0; //삭제가능 //본인상품
        } else {
            status = 1;
        }

        List<String> productImgs = pir.findProductImageUrlByProductNo(productNo);

        Map<String,Object> result = new HashMap<>();
        result.put("product", product);
        result.put("productImgs", productImgs);
        result.put("status", status);

        return result;
    }

//    이미지수정X
    public void updateProduct(ProductEntity product) {
        Optional<ProductEntity> optP = pr.findById(product.getProductNo());

        ProductEntity p = optP.get();
        p.setTitle(product.getTitle());
        p.setContent(product.getContent());
        p.setPriceCategory(product.getPriceCategory());
        p.setPrice(product.getPrice());
        p.setEndDate(product.getEndDate());
        pr.save(p);
    }

//    이미지수정
//    public void updateProduct(ProductEntity product, List<MultipartFile> files) throws IOException {
//        Long productNo = product.getProductNo();
//        Optional<ProductEntity> optP = pr.findById(productNo);
//
//        pir.deleteByProductNo(productNo);
//
//        MultipartFile first = files.get(0);
//        String thumbnailUrl = null;
//        if(first.getSize() > 0) {
//            thumbnailUrl = s3Service.upload(files.get(0));
//        }
//
//        ProductEntity p = optP.get();
//
//        p.setThumbnail(thumbnailUrl);
//        p.setTitle(product.getTitle());
//        p.setContent(product.getContent());
//        p.setPriceCategory(product.getPriceCategory());
//        p.setPrice(product.getPrice());
//        p.setEndDate(product.getEndDate());
//        pr.save(p);
//
//        List<MultipartFile> additionalFiles = files.subList(1, files.size());
//        for (MultipartFile additionailFile : additionalFiles) {
//            String additionalImageUrl = s3Service.upload(additionailFile);
//            ProductImageEntity pi = ProductImageEntity.builder()
//                    .productImageUrl(additionalImageUrl)
//                    .product(p)
//                    .build();
//            pir.save(pi);
//        }
//    }

    @Scheduled(cron="59 59 23 * * *")
    public void changeStatusOverdated() {
        LocalDate todayDate = LocalDate.now();
        Date endDate = java.sql.Date.valueOf(todayDate);
        List<ProductEntity> products = pr.findByEndDate(endDate);
        for (ProductEntity product : products) {
            product.setStatus(2);
        }

        pr.saveAll(products);
    }

    public void changeStatusRented(Long productNo) {
        Optional<ProductEntity> optP = pr.findById(productNo);
        ProductEntity p = optP.get();
        p.setStatus(1);
        pr.save(p);
//        pr.updateStatusTo1(productNo);
    }

    public void deleteProductById(Long productNo) {
        pr.deleteById(productNo);
    }

    public void deleteProductByNickname(String nickname) { pr.deleteByNickname(nickname);}



    public Map<String, Object> productList(String types,Request request) {
        String searchWord = request.getSearchWord();
        String siDo = request.getSiDo();
        String siGunGu = request.getSiGunGu();
        String eupMyeonRo = request.getEupMyeonRo();
        String categoryName = request.getCategoryName();

        Map map = pr.findProduct(types,
                                 searchWord,
                                 siDo, siGunGu, eupMyeonRo,
                                 categoryName);

        List<ProductEntity> product = (List<ProductEntity>) map.get("productList");

        List<Product> result = product.stream()
                                      .map(p -> modelMapper.map(p, Product.class))
                                      .collect(Collectors.toList());

        map.put("productList", result);

        return map;
    }

    public List<Product> myProduct(String nickname, Integer postCategory) {
        List<ProductEntity> product = pr.findByNicknameAndPostCategory(nickname, postCategory);
        List<Product> result = product.stream()
                .map(p -> modelMapper.map(p, Product.class))
                .collect(Collectors.toList());

        return result;
    }


//    public Long averagePrice(List<ProductEntity> productList) {
//        return pr.calculateAveragePrice(productList);
//    }

    /*
    public Product backupSelectProduct(Long productNo) {
        Optional<ProductEntity> productEntity = pr.findById(productNo);
        if (productEntity == null) {
            throw new EntityNotFoundException("Product not found for productNo: " + productNo);
        }

        Product result = modelMapper.map(productEntity, Product.class);

//        Product product = new Product();
//        product.setProduct_no(productEntity.getProductNo());
//        product.setNickname(productEntity.getNickname());
//        product.setTitle(productEntity.getTitle());
//        product.setContent(productEntity.getContent());
//        product.setPrice(productEntity.getPrice());
//        product.setStartDate(productEntity.getStartDate());
//        product.setEndDate(productEntity.getEndDate());
//        product.setStatus(productEntity.getStatus());
//        product.setPostDate(productEntity.getPostDate());
//        product.setViewCount(productEntity.getViewCount());
//        product.setRentCount(productEntity.getRentCount());
//        product.setPostCategory(productEntity.getPostCategory());
//        product.setPriceCategory(productEntity.getPriceCategory());

        // Address와 Category도 매핑
//        product.setCategory(mapCategoryEntityToDto(productEntity.getCategory()));
//        product.setAddress(mapAddressEntityToDto(productEntity.getAddress()));
        return result;
    }
     */


}
