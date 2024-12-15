package com.example.abhyasa.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDQuestionReq {
   long dQid;

   String status;
   byte[] photo;
}
