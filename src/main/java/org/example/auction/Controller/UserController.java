package org.example.auction.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auction.Api.ApiResponse;
import org.example.auction.Model.*;
import org.example.auction.Repository.SellerRepository;
import org.example.auction.Service.RatingService;
import org.example.auction.Service.SellerService;
import org.example.auction.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
     private final UserService userService;


    @Autowired
    private RatingService ratingService;


    private final SellerService sellerService;

    @GetMapping("/get")
    public ResponseEntity getAllUsers() {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }


    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id, @RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        userService.updateUser(id, user);
        return ResponseEntity.ok(new ApiResponse("User updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted"));
    }





    //Extra

    // in RatingService

    @PostMapping("/add-rating")
    public ResponseEntity addRating(@RequestBody Rating request) {
        ratingService.addRatingAndCalculateAverage(request.getSellerId(), request.getUserId(), request.getValue());
        return ResponseEntity.ok("Rating added");
    }


    @GetMapping("/sellers")
    public List<Seller> getAllSellersWithRating() {
        return ratingService.getAllSellersWithRating();
    }



    @GetMapping("/highest-rated-seller")
    public ResponseEntity<Seller> findHighestRatedSeller() {
        Seller highestRatedSeller = ratingService.findSellerWithHighestRating();
        if (highestRatedSeller == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(highestRatedSeller);
    }


    @GetMapping("/{sellerId}/ratings")
    public ResponseEntity<List<Rating>> getRatingsForSeller(@PathVariable Integer sellerId) {
        List<Rating> ratings = ratingService.getRatingsForSeller(sellerId);
        return ResponseEntity.ok(ratings);
    }


    @GetMapping("/{userId}/purchased-products")
    public ResponseEntity<List<Product>> getPurchasedProducts(@PathVariable Integer userId) {
        List<Product> purchasedProducts = userService.getPurchasedProducts(userId);
        return ResponseEntity.ok(purchasedProducts);
    }




    @PostMapping("/{userId}/deposit")
    public ResponseEntity depositToUserWallet(@PathVariable Integer userId, @RequestBody DepositRequest depositRequest) {
        userService.depositToUserWallet(userId, depositRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Deposit successful");
    }



    // لإجراء سحب من المحفظة
    @PostMapping("/{userId}/withdraw")
    public ResponseEntity withdrawFromUserWallet(@PathVariable Integer userId, @RequestBody DepositRequest withdrawRequest) {
        userService.withdrawFromUserWallet(userId, withdrawRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Withdrawal successful");
    }

    @GetMapping("/{userId}/walletBalance")
    public ResponseEntity<BigDecimal> getWalletBalance(@PathVariable Integer userId) {
        BigDecimal walletBalance = userService.getWalletBalance(userId);
        return ResponseEntity.ok(walletBalance);
    }





}
