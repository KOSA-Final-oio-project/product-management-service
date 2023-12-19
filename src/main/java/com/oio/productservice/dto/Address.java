package com.oio.productservice.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private Long addressNo;
    private String siDo;
    private String siGunGu;
    private String eupMyeonRo;

}
