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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "cant be empty")
    @Column(columnDefinition = "varchar(10) not null")
    private String name;

    @NotNull(message = "cant be null")
    @Column(columnDefinition = "varchar(20) not null")
    private String password;

    @NotEmpty(message = "can't be empty")
    @Email
    @Column(columnDefinition = "varchar(20) not null unique")
    private String email;


    @OneToMany(mappedBy = "seller_Id")
    private List<Product> purchasedProducts;

    // طريقة لإضافة منتج إلى قائمة المنتجات التي اشتراها المستخدم
    public void addPurchasedProduct(Product product) {
        if (purchasedProducts == null) {
            purchasedProducts = new ArrayList<>();
        }
        purchasedProducts.add(product);
    }


    @Column(columnDefinition = "decimal(10,2) default 0.0")
    private BigDecimal wallet;


    // طريقة لإيداع أموال في المحفظة
    public void depositToWallet(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            wallet = wallet.add(amount);

        } else {
            throw new IllegalArgumentException("Invalid amount for deposit");
        }
    }

    // طريقة لسحب أموال من المحفظة
    public void withdrawFromWallet(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0 && wallet.compareTo(amount) >= 0) {
            wallet = wallet.subtract(amount);

        } else {
            throw new IllegalArgumentException("Invalid amount for withdrawal or insufficient balance");
        }
    }
}