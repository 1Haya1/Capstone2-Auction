package org.example.auction.Service;

import lombok.RequiredArgsConstructor;
import org.example.auction.Api.ApiException;
import org.example.auction.Model.*;
import org.example.auction.Repository.SellerRepository;
import org.example.auction.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }


    public void updateUser(Integer id, User user) {
        User existingUser = userRepository.findUserById(id);
        if (existingUser == null) {
            throw new ApiException("User not found");
        }
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setName(user.getName());

        userRepository.save(existingUser);
    }

    public void deleteUser(Integer id) {
        User existingUser = userRepository.findUserById(id);
        if (existingUser == null) {
            throw new ApiException("User not found");
        }
        userRepository.delete(existingUser);
    }



    //Extra + Rating by user in RatingService

    public List<Product> getPurchasedProducts(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user != null) {
            return user.getPurchasedProducts();
        } else {
            throw new RuntimeException("User not found");
        }
    }




    public void depositToUserWallet(Integer userId, DepositRequest depositRequest) {
        BigDecimal amount = depositRequest.getAmount();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal currentBalance = user.getWallet();
        BigDecimal newBalance = currentBalance.add(amount); // زيادة المبلغ المودع على المبلغ الحالي في المحفظة

        user.setWallet(newBalance); // تحديث قيمة المحفظة للمستخدم
        userRepository.save(user);
    }



    public void withdrawFromUserWallet(Integer userId, DepositRequest withdrawRequest) {
        BigDecimal amount = withdrawRequest.getAmount();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.withdrawFromWallet(amount);
        userRepository.save(user);
    }


    public BigDecimal getWalletBalance(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getWallet();
    }

}
