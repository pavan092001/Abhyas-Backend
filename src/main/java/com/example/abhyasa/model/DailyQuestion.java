package com.example.abhyasa.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DailyQuestion {



    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    long dQId;

    @ManyToOne
    @JoinColumn(name = "uid",nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "qId",nullable = false)
    Question question;




    @CreatedDate
    Date assignedDate;

    boolean isCompleted;

    @Lob
    byte[] photo;

}
