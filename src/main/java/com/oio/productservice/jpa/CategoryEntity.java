package com.oio.productservice.jpa;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CATEGORY")
@SequenceGenerator(
        name = "SEQ_GENERATOR",
        sequenceName = "CATEGORY_SEQ",
        allocationSize = 1
)
public class CategoryEntity {

    @Id
    @Column(name = "CATEGORY_NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    private Long categoryNo;

    @Column(nullable = false, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<ProductEntity> products = new ArrayList<>();

}
