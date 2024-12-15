package com.example.abhyasa.response;


import com.example.abhyasa.model.Question;
import com.example.abhyasa.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DQuesResponse {

    long dQId;


    Question question;


    Date assignedDate;

    boolean isCompleted;


    byte[] photo;


    @Override
    public String toString() {
        return "DQuesResponse{" +
                "dQId=" + dQId +
                ", question=" + question +
                ", assignedDate=" + assignedDate +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
