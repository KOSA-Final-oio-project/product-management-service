package com.oio.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class ProductImage {
    private Long productImageNo;
    private String productImageUrl;
    private Product product;
}
