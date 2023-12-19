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
@Table(name = "ADDRESS")
@SequenceGenerator(
        name = "SEQ_GENERATOR",
        sequenceName = "ADDRESS_SEQ",
        allocationSize = 1
)
public class AddressEntity {

    @Id
    @Column(name = "ADDRESS_NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    private Long addressNo;

    @Column(nullable = false)
    private String siDo;

    @Column
    private String siGunGu;

    @Column
    private String eupMyeonRo;

    @OneToMany(mappedBy = "address")
    private List<ProductEntity> products = new ArrayList<>();
}
