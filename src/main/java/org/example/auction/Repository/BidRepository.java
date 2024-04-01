package org.example.auction.Repository;

import org.example.auction.Model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid,Integer> {

    @Query("SELECT b FROM Bid b WHERE b.auctionId = :auctionId")
    List<Bid> findByAuctionId(Integer auctionId);

}
