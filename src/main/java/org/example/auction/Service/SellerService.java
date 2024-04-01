package org.example.auction.Service;

import lombok.RequiredArgsConstructor;
import org.example.auction.Api.ApiException;

import org.example.auction.Model.Product;
import org.example.auction.Model.Rating;
import org.example.auction.Model.Seller;
import org.example.auction.Model.User;
import org.example.auction.Repository.ProductRepository;
import org.example.auction.Repository.RatingRepository;
import org.example.auction.Repository.SellerRepository;
import org.example.auction.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

        public List<Seller> getAllSellers() {
            return sellerRepository.findAll();
        }

    public void addSeller(Seller seller) {
         sellerRepository.save(seller);

    }
    public Seller getSellerById(Integer id) {
        return sellerRepository.findSellerById(id);
    }


    public void updateSeller(Integer id, Seller seller) {
        Seller c = sellerRepository.findSellerById(id);

        if (c == null) {
            throw new ApiException("wrong id");
        }

        c.setEmail(seller.getEmail());
        c.setPassword(seller.getPassword());
        c.setName(seller.getName());
        c.setProducts(seller.getProducts());
        c.setPhoneNumber(seller.getPhoneNumber());
        c.setRatings(seller.getRatings());
        sellerRepository.save(c);
    }

    public void deleteSeller(Integer id) {
        Seller c = sellerRepository.findSellerById(id);
        if (c == null) {
            throw new ApiException("wrong id");
        }
        sellerRepository.delete(c);
    }




    //Extra



    public Map<Seller, List<Product>> findAllSellersWithActiveProducts() {
        List<Seller> sellers = sellerRepository.findAll();
        Map<Seller, List<Product>> sellersWithActiveProducts = new HashMap<>();
        for (Seller seller : sellers) {
            List<Product> activeProducts = new ArrayList<>();
            for (Product product : seller.getProducts()) {
                if (product.isActive()) {
                    activeProducts.add(product);
                }
            }
            if (!activeProducts.isEmpty()) {
                sellersWithActiveProducts.put(seller, activeProducts);
            }
        }
        return sellersWithActiveProducts;
    }





    public List<Product> getSellersWithActiveProducts(Integer sellerId) {// بائع واحد
        List<Product> products = productRepository.findBySeller_Id(sellerId);
        List<Product> activeProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.isActive()) {
                activeProducts.add(product);
            }
        }
        return activeProducts;
    }



    public List<Seller> findTopSellingSellers(int limit) {
        // البحث عن البائعين وتحميل قائمة المنتجات المباعة لكل بائع
        List<Seller> sellers = sellerRepository.findAll();
        for (Seller seller : sellers) {
            int inactiveSoldProductCount = 0;
            for (Product product : seller.getSoldProducts()) {
                if (!product.isActive()) {
                    inactiveSoldProductCount++;
                }
            }
            seller.setSoldProductCount(inactiveSoldProductCount);
        }

        // ترتيب البائعين حسب عدد المنتجات الغير النشطة المباعة
        sellers.sort(Comparator.comparingInt(Seller::getSoldProductCount).reversed());

        // الحد من عدد البائعين الذين سيتم عرضهم
        if (limit > sellers.size()) {
            limit = sellers.size();
        }

        // استرجاع أعلى البائعين المبيعا
        return sellers.subList(0, limit);
    }



}
