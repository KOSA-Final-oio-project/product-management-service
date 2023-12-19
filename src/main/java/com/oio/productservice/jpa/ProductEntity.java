package com.oio.productservice.jpa;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = ProductEntity.NamedQuery_IncreaseViewCount, //increaseViewCount
                procedureName = "increase_view_count",
                resultClasses = ProductEntity.class,
                parameters = {
                        @StoredProcedureParameter(type = Long.class, mode = ParameterMode.IN),
//        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "role_list_o", type = Void.class)
                        @StoredProcedureParameter(type = void.class, mode = ParameterMode.REF_CURSOR)
                })
})

@DynamicInsert
@Entity
@Table(name = "product")
@SequenceGenerator(
        name = "SEQ_GENERATOR",
        sequenceName = "PRODUCT_SEQ",
        allocationSize = 1
)
public class ProductEntity {
    public static final String NamedQuery_IncreaseViewCount = "increaseViewCount";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    private Long productNo;

    @Column(nullable = false)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_NO")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_NO")
    private AddressEntity address;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column
    @ColumnDefault("0")
    //0-미대여 1-대여중 2-기간만료
    private Integer status;

    @Column
    @ColumnDefault(value = "SYSDATE")
    private Date postDate;

    @Column
    @ColumnDefault("0")
    private Integer viewCount;

    @Column
    @ColumnDefault("0")
    private Integer rentCount;

    @Column(nullable = false)
    //0-빌려드립니다 1-빌려주세요
    private Integer postCategory;

    @Column(nullable = false)
    private String priceCategory;

    @Column
    private String thumbnail;

    public void updateStatus(Integer status) {
        this.status = status;
    }

    public void updateRentCount(Integer rentCount) {
        this.rentCount += rentCount;
    }

    @OneToMany(mappedBy = "product")
    private List<ProductImageEntity> productImages = new ArrayList<>();

}
