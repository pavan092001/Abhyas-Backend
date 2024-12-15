package com.example.abhyasa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private  String name;
    private String email;
    private String role;
    private boolean isTwoFactorEnabled = false;
    private Date createdDate;
    private Date updatedDate;


}
