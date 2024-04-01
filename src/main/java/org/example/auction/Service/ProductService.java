package org.example.auction.Service;

import lombok.RequiredArgsConstructor;
import org.example.auction.Api.ApiException;
import org.example.auction.Model.*;
import org.example.auction.Repository.BidRepository;
import org.example.auction.Repository.ProductRepository;
import org.example.auction.Repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;


    private final SellerRepository sellerRepository;
    private final BidRepository bidRepository;
    private final AuctionService auctionService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public void addProduct(Product product) {
        // التحقق من وجود البائع
        Seller seller = sellerRepository.findById(product.getSeller_Id())
                .orElseThrow(() -> new ApiException("Invalid seller id"));


        product.setActive(product.isActive());
        product.setSeller_Id(product.getSeller_Id());
        product.setStarting_Price(product.getStarting_Price());
        product.setName(product.getName());
        product.setDescription(product.getDescription());

        // حفظ المنتج
        productRepository.save(product);
    }


    public void updateProduct(Integer id, Product product) {
        Product p = productRepository.findProductById(id);

        if (p == null) {
            throw new ApiException("wrong id");
        }

        p.setName(product.getName());
        p.setDescription(product.getDescription());
        p.setSeller_Id(product.getSeller_Id());
        p.setStarting_Price(product.getStarting_Price());

        p.setActive(product.isActive());

        productRepository.save(p);
    }

    public void deleteProduct(Integer id) {
        Product p = productRepository.findProductById(id);
        if (p == null) {
            throw new ApiException("wrong id");
        }
        productRepository.delete(p);

    }


    //Extra


    public List<Product> findByMinimumPrice(List<Product> productList, Integer minPrice) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : productList) {
            if (product.getStarting_Price() >= minPrice) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }


    public List<Product> findProductsBySellerId(Integer sellerId) { // كل المنتجات النشطه والغير نشطه

        List<Product> products = productRepository.findBySeller_Id(sellerId);

        return products;
    }



    //   لحساب عدد المنتجات الموجودة في المزاد
    public int countProductsInAuction(Integer productId) {
        List<Integer> auctionIds = auctionService.findAuctionIdsByProductId(productId);
        int totalCount = 0;

        for (Integer auctionId : auctionIds) {
            int productCountInAuction = auctionService.countProductsInAuction(auctionId);
            totalCount += productCountInAuction;
        }

        return totalCount;
    }



}












