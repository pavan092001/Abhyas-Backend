package com.example.abhyasa.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {


    private String email;
    private  String name;
    private String password;
    private String role;





}
