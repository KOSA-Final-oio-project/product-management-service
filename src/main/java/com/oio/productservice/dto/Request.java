package com.oio.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class Request {
    private String searchWord;
    private String siDo;
    private String siGunGu;
    private String eupMyeonRo;
    private String categoryName;
    private Integer postCategory;
}
