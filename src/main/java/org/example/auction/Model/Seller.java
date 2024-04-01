package org.example.auction.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "cant be empty")
    @Column(columnDefinition = "varchar(10) not null")
    private String name;


    @NotNull(message = "cant be null")
    @Column(columnDefinition = "int not null")
    private String product_Id;



    @NotNull(message = "cant be null")
    @Column(columnDefinition = "int not null")
    private String phoneNumber;

    @NotNull(message = "cant be null")
    @Column(columnDefinition = "varchar(20) not null")
    private String password;

    @NotEmpty(message = "can't be empty")
    @Email
    @Column(columnDefinition = "varchar(20) not null unique")
    private String email;


    private Double averageRating;

    @OneToMany(mappedBy = "sellerId")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "seller_Id", cascade = CascadeType.ALL)
    private List<Product> products;


    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "seller_Id")
    private List<Product> soldProducts;

    public void addToSoldProducts(Product product) {
        if (soldProducts == null) {
            soldProducts = new ArrayList<>();
        }}

    @Transient
    private int soldProductCount;

}