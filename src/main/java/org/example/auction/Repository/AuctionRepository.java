package org.example.auction.Repository;

import org.example.auction.Model.Auction;
import org.example.auction.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Integer> {

    @Query("select c from Auction c  where c.id=?1")
    Auction findAuctionById(Integer id);


    @Query("SELECT a.id FROM Auction a WHERE a.product_Id = :productId")
    List<Integer> findAuctionIdsByProductId(@Param("productId") Integer productId);

}
