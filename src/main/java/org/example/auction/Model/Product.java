package org.example.auction.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "cant be empty")
    @Column(columnDefinition ="varchar(10) not null" )
    private String name;

    @NotEmpty(message = "cant be empty")
    @Column(columnDefinition = "text not null")
    private String description;



    @NotNull
    private Integer seller_Id;


    @NotNull(message = "cant be null")
    @Column(columnDefinition = " int not null") // السعر الابتدائي
    private Integer starting_Price;



    @Column(columnDefinition = "boolean not null")
    private boolean active;


    // تقييم الجودة للمنتج من قبل البائع
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private ProductQuality sellerQualityRating;

    public enum ProductQuality {
        EXCELLENT,
        VERY_GOOD,
        GOOD,
        ACCEPTABLE,
        POOR
    }

}

