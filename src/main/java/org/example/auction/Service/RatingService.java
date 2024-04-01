package org.example.auction.Service;

import lombok.RequiredArgsConstructor;
import org.example.auction.Model.Rating;
import org.example.auction.Model.Seller;
import org.example.auction.Model.User;
import org.example.auction.Repository.RatingRepository;
import org.example.auction.Repository.SellerRepository;
import org.example.auction.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {



    @Autowired
    private SellerRepository sellerRepository;


    private final UserRepository userRepository;

    private final RatingRepository ratingRepository;


// in UserController
    public void addRatingAndCalculateAverage(Integer sellerId, Integer userId, Integer value) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("Rating value must be between 1 and 5.");
        }
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null) {
            throw new RuntimeException("Seller not found");
        }

        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Rating rating = new Rating();
        rating.setSellerId(sellerId);
        rating.setUserId(userId);
        rating.setValue(value);

        ratingRepository.save(rating);

        double averageRating = calculateAverageRating(seller);
        seller.setAverageRating(averageRating);
        sellerRepository.save(seller);
    }

public double calculateAverageRating(Seller seller) {
    List<Rating> ratings = seller.getRatings();
    if (ratings.isEmpty()) {
        return 0.0;
    }

    double sum = 0.0;
    for (Rating rating : ratings) {
        sum += rating.getValue();
    }
    return sum / ratings.size();
    }






    public List<Seller> getAllSellersWithRating() {
        List<Seller> sellers = sellerRepository.findAll();
        for (Seller seller : sellers) {
            double averageRating = calculateAverageRating(seller);
            seller.setAverageRating(averageRating);
        }
        return sellers;
    }



    public Seller findSellerWithHighestRating() {
        List<Seller> sellers = sellerRepository.findAll();

        Seller sellerWithHighestRating = null;
        double highestRating = 0.0;

        for (Seller seller : sellers) {
            double averageRating = calculateAverageRating(seller);
            if (averageRating > highestRating) {
                highestRating = averageRating;
                sellerWithHighestRating = seller;
            }
        }

        return sellerWithHighestRating;
    }


    public List<Rating> getRatingsForSeller(Integer sellerId) {
        // الحصول على تقييمات البائع
        List<Rating> sellerRatings = ratingRepository.findBySeller_Id(sellerId);

        // الحصول على تقييمات المستخدمين للبائع
        List<Rating> userRatings = ratingRepository.findByUserId(sellerId);

        // دمج تقييمات البائع وتقييمات المستخدمين
        sellerRatings.addAll(userRatings);


        return sellerRatings;
    }


}
