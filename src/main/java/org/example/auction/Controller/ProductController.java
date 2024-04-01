package org.example.auction.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auction.Api.ApiResponse;
import org.example.auction.Model.Bid;
import org.example.auction.Model.Product;
import org.example.auction.Service.ProductService;
import org.example.auction.Service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final SellerService sellerService;


    @GetMapping("/get")
    public ResponseEntity getAllProducts() {
        return ResponseEntity.status(200).body(productService.getAllProducts());
    }


    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        productService.addProduct(product);
        return ResponseEntity.status(200).body(new ApiResponse("Product added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id, @RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        productService.updateProduct(id, product);
        return ResponseEntity.status(200).body(new ApiResponse("Product updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(200).body(new ApiResponse("Product deleted"));
    }


    //Etxra


    @GetMapping("/products/minimum-price")
    public ResponseEntity<List<Product>> findByMinimumPrice(@RequestParam("minPrice") Integer minPrice) {
        List<Product> products = productService.findByMinimumPrice(productService.getAllProducts(), minPrice);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }



    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Product>> getProductsBySellerId(@PathVariable Integer sellerId) {
        List<Product> products = productService.findProductsBySellerId(sellerId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{productId}/auctions/count")
    public int countProductsInAuction(@PathVariable Integer productId) {
        return productService.countProductsInAuction(productId);
    }

}

