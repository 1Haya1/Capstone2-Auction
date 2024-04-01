package org.example.auction.Repository;

import org.example.auction.Model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Integer> {


    @Query("select c from Seller c  where c.id=?1")
    Seller findSellerById(Integer id);

    @Query("SELECT DISTINCT s FROM Seller s JOIN FETCH s.products p WHERE p.active = true")
    List<Seller> findAllWithActiveProducts();

}
