package org.example.auction.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auction.Api.ApiResponse;
import org.example.auction.Model.*;
import org.example.auction.Repository.ProductRepository;
import org.example.auction.Repository.SellerRepository;
import org.example.auction.Service.AuctionService;
import org.example.auction.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionService auctionService;
    private final ProductService productService;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;


    @GetMapping("/get")
    public ResponseEntity getAllAuctions() {
        return ResponseEntity.status(200).body(auctionService.getAllAuctions());
    }

    @PostMapping("/add-auction")
    public ResponseEntity addAuction(@RequestBody @Valid Auction auction, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        auctionService.addAuction(auction);
        return ResponseEntity.status(200).body(new ApiResponse("added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateAuction(@PathVariable Integer id, @RequestBody @Valid Auction auction, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        auctionService.updateAuction(id, auction);
        return ResponseEntity.status(200).body(new ApiResponse("updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteAuction(@PathVariable Integer id) {
        auctionService.deleteAuction(id);
        return ResponseEntity.status(200).body(new ApiResponse("deleted"));
    }


    //Extra




    @PostMapping("/{auctionId}/bids")
    public ResponseEntity addBidsToAuction(@PathVariable Integer auctionId, @RequestBody BidRequest bidRequest) {
        List<User> users = bidRequest.getUsers();
        List<BigDecimal> prices = bidRequest.getPrices();

        if (users == null || prices == null || users.size() != prices.size()) {
            return ResponseEntity.badRequest().body("Invalid request");
        }

        auctionService.addBidsToAuction(auctionId, users, prices);
        return ResponseEntity.ok("Bids added successfully");
    }



    @GetMapping("/auction/{auctionId}/winning-bid")
    public ResponseEntity getWinningBid(@PathVariable Integer auctionId) {
        Optional<Bid> winningBidOptional = auctionService.getWinningBid(auctionId);

        if (winningBidOptional.isPresent()) {
            Bid winningBid = winningBidOptional.get();
            return ResponseEntity.ok(winningBid);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        @PostMapping("/complete/{auctionId}")
        public ResponseEntity completeAuction(@PathVariable Integer auctionId) {
            auctionService.completeAuction(auctionId);
            return ResponseEntity.ok("Auction completed successfully");
        }

    @GetMapping("/ended/count")
    public int countEndedAuctions() {
        return auctionService.countEndedAuctions();
    }



}
