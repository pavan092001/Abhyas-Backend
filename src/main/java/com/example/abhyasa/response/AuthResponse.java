package com.example.abhyasa.response;

import com.example.abhyasa.dto.UserDto;
import com.example.abhyasa.model.User;
import com.example.abhyasa.request.SignUpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String message;
    private String jwt;
    UserDto user;
    private String role;
    boolean status;
    String csrfToken;



}



