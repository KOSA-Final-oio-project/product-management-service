package com.oio.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Product {
    private Long productNo;
    private String nickname;
    private String title;
    private String content;
    private Integer price;
    private String startDate;
    private String endDate;
    private Integer status;
    private Date postDate;
    private Integer viewCount;
    private Integer rentCount;
    private Integer postCategory;
    private String priceCategory;
    private String thumbnail;
    private Address address;
    private Category category;
}
