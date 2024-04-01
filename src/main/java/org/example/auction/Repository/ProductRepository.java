package org.example.auction.Repository;

import org.example.auction.Model.Auction;
import org.example.auction.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {


    @Query("select c from Product c  where c.id=?1")
    Product findProductById(Integer id);

    Product getProductById(Integer id);

    @Query("SELECT p FROM Product p WHERE p.starting_Price >= :minPrice")
    List<Product> findByMinimumPrice(@Param("minPrice") Integer minPrice);

    @Query("SELECT p FROM Product p WHERE p.seller_Id = :seller_Id")
    List<Product> findBySeller_Id(@Param("seller_Id") Integer seller_Id);

}


