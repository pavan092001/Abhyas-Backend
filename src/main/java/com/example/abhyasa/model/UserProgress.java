package com.example.abhyasa.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "uId", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;


    private int totalAssigned;


    private int solvedCount;

    // Getters and setters
}
