package org.example.auction.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull(message = "cant be null")
    @ManyToMany
    private List<User> users;

    @NotNull(message = "cant be null")
    @Column(columnDefinition = "int not null")
    private Integer product_Id;

    @NotNull(message = "cant be null")
    @Column(columnDefinition = "int not null")
    private Integer seller_Id;



    @Column(columnDefinition = "date")
    private LocalDateTime startTime;

    @Column(columnDefinition = "date")
    private LocalDateTime endTime;


}
