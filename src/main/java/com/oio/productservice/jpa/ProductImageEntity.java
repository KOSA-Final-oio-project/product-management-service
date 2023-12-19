package com.oio.productservice.jpa;


import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "product_image")
@SequenceGenerator(
        name = "SEQ_GENERATOR",
        sequenceName = "PRODUCT_IMAGE_SEQ",
        allocationSize = 1
)
public class ProductImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    private Long productImageNo;

    @Column
    private String productImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_NO")
    private ProductEntity product;

}
