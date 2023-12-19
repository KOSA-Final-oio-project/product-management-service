package com.oio.productservice.dto;

import lombok.*;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Category {
    private Long categoryNo;

    private String categoryName;
}
