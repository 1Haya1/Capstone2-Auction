package org.example.auction.Repository;

import org.example.auction.Model.Seller;
import org.example.auction.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("select c from User c  where c.id=?1")
    User findUserById(Integer id);

}
