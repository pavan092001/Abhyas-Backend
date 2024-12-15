package com.example.abhyasa.service;

import com.example.abhyasa.model.User;
import com.example.abhyasa.request.LoginRequest;
import com.example.abhyasa.request.SignUpRequest;
import com.example.abhyasa.response.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {


    public AuthResponse signUp(SignUpRequest sign);


    public  AuthResponse signIn(AuthenticationManager authenticationManager,LoginRequest req);

}
