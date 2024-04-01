package org.example.auction.Service;

import lombok.RequiredArgsConstructor;
import org.example.auction.Api.ApiException;
import org.example.auction.Model.*;
import org.example.auction.Repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;



    public void addAuction(Auction auction) {
        auction.setStartTime(LocalDateTime.now()); // تعيين التوقيت الحالي كبداية المزاد
        auctionRepository.save(auction);
    }
    public void updateAuction(Integer id, Auction auction) {
        Auction a = auctionRepository.findAuctionById(id);
        if (a == null) {
            throw new ApiException("wrong id");
        }
        a.setStartTime(auction.getStartTime());
        a.setEndTime(auction.getEndTime());
        a.setUsers(auction.getUsers());
        a.setSeller_Id(auction.getSeller_Id());
        a.setProduct_Id(auction.getProduct_Id());
        auctionRepository.save(a);
    }


    public void deleteAuction(Integer id) {
        Auction a = auctionRepository.findAuctionById(id);
        if (a == null) {
            throw new ApiException("wrong id");
        }
        auctionRepository.delete(a);
    }



    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }





    //Extra


    public void addBidsToAuction(Integer auctionId, List<User> users, List<BigDecimal> prices) {
        // استرداد المزاد
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        if (users.size() != prices.size()) {
            throw new IllegalArgumentException("Number of users must match number of prices");
        }
        // الحصول على السعر الابتدائي للمنتج
        Product product = productRepository.findById(auction.getProduct_Id())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Integer starting_Price = product.getStarting_Price();

        // إضافة عرض لكل مستخدم
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            BigDecimal price = prices.get(i);

            // التحقق من أن السعر الذي قدمه المستخدم أعلى من السعر الابتدائي
            if (price.compareTo(BigDecimal.valueOf(starting_Price)) <= 0) {
                throw new IllegalArgumentException("Bid price must be higher than starting price");
            }

            Bid bid = new Bid();
            bid.setAuctionId(auctionId);
            bid.setUsers(users); // تعيين معرف المستخدم
            bid.setPrice(price);


            bidRepository.save(bid);
        }
        // تعيين وقت انتهاء المزاد إلى null حتى يتم استدعاء completeAuction
        auction.setEndTime(null);
        auctionRepository.save(auction);
    }



    //   العرض الفائز النهائي
    public Optional<Bid> getWinningBid(Integer auctionId) {
        // استرداد قائمة العروض للمزاد
        List<Bid> bids = bidRepository.findByAuctionId(auctionId);

        //    الذي يحمل أعلى سعر
        return bids.stream()
                .max(Comparator.comparing(Bid::getPrice));
    }



    public void completeAuction(Integer auctionId) {
        // العثور على المزاد
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        // التحقق مما إذا كان المزاد قد انتهى بالفعل
        if (auction.getEndTime() != null) {
            throw new RuntimeException("Auction already completed");
        }

        // العثور على العرض الفائز
        Optional<Bid> winningBidOptional = getWinningBid(auctionId);
        if (!winningBidOptional.isPresent()) {
            throw new RuntimeException("No winning bid found for auction");
        }
        Bid winningBid = winningBidOptional.get();

        // تحويل السعر الفائز إلى البائع
        BigDecimal winningPrice = winningBid.getPrice();
        Seller seller = sellerRepository.findById(auction.getSeller_Id())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // خصم المبلغ الفائز من محفظة المستخدم
        for (User user : winningBid.getUsers()) {
            user.withdrawFromWallet(winningPrice);
            userRepository.save(user);
            // إضافة المنتج الفائز إلى قائمة المشتريات السابقة للمستخدم
            Product product = productRepository.findById(auction.getProduct_Id())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            user.addPurchasedProduct(product);
            userRepository.save(user);

        }

        seller.setBalance(seller.getBalance().add(winningPrice));
        sellerRepository.save(seller);

        // تحديث حالة المنتج لتصبح غير نشطة
        Product product = productRepository.findById(auction.getProduct_Id())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);

        // إضافة المنتجات المباعة لكاونت البائع
        seller.addToSoldProducts(product);
        sellerRepository.save(seller);



        // تحديث المزاد ليتم توقيفه
        auction.setEndTime(LocalDateTime.now());
        auctionRepository.save(auction);
    }




    public List<Integer> findAuctionIdsByProductId(Integer productId) {// في ProductService
        return auctionRepository.findAuctionIdsByProductId(productId);
    }
    public int countProductsInAuction(Integer productId) {
        List<Integer> auctionIds = auctionRepository.findAuctionIdsByProductId(productId);
        return auctionIds.size();
    }


    public int countEndedAuctions() {
        // استرداد جميع المزادات
        List<Auction> auctions = auctionRepository.findAll();

        // تعداد المزادات التي انتهت
        int endedAuctionsCount = 0;
        for (Auction auction : auctions) {
            if (auction.getEndTime() != null) {
                endedAuctionsCount++;
            }
        }

        return endedAuctionsCount;
    }




}
