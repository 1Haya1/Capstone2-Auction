package org.example.auction.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auction.Api.ApiResponse;
import org.example.auction.Model.Product;
import org.example.auction.Model.Seller;
import org.example.auction.Service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;


    @GetMapping("/get")
    public ResponseEntity getAllSellers() {
        return ResponseEntity.status(200).body(sellerService.getAllSellers());
    }


    @PostMapping("/add")
    public ResponseEntity addSeller(@RequestBody @Valid Seller seller, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        sellerService.addSeller(seller);
        return ResponseEntity.status(200).body(new ApiResponse("Seller added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateSeller(@PathVariable Integer id, @RequestBody @Valid Seller seller, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        sellerService.updateSeller(id, seller);
        return ResponseEntity.ok(new ApiResponse("Seller updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteSeller(@PathVariable Integer id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.status(200).body(new ApiResponse("Seller deleted"));
    }


    @GetMapping("/get-seller/{id}")
    public ResponseEntity getSellerById(@PathVariable Integer id) {
        Seller seller = sellerService.getSellerById(id);
        if (seller == null) {
            return ResponseEntity.status(400).body(new ApiResponse("id not found"));
        }
        return ResponseEntity.ok(seller);
    }






    //Extra


    @GetMapping("/sellers/active-products")
    public Map<Seller, List<Product>> findAllSellersWithActiveProducts() {
        return sellerService.findAllSellersWithActiveProducts();
    }

    @GetMapping("/get/{sellerId}/products-active")
    public ResponseEntity<List<Product>> getSellersWithActiveProducts(@PathVariable Integer sellerId) {
        List<Product> activeProducts = sellerService.getSellersWithActiveProducts(sellerId);
        return ResponseEntity.ok().body(activeProducts);
    }

    @GetMapping("/top-sellers")
    public List<Seller> getTopSellers(@RequestParam(name = "limit", defaultValue = "10") int limit) {
        return sellerService.findTopSellingSellers(limit);
    }









}
