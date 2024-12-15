package com.example.abhyasa.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;



    private String tokenType;


    private boolean expired;
    private  boolean revoked;


    @ManyToOne
    @JoinColumn(name = "user_uid")
    private  User user;



}
