package org.example.auction.Repository;

import org.example.auction.Model.Product;
import org.example.auction.Model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Integer> {

    @Query("SELECT p FROM Rating p WHERE p.sellerId = :seller_Id")
    List<Rating> findBySeller_Id(@Param("seller_Id") Integer seller_Id);

    @Query("SELECT r FROM Rating r WHERE r.userId  = :userId")
    List<Rating> findByUserId(@Param("userId") Integer userId);
}
