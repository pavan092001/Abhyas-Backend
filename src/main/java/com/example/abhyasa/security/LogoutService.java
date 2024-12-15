package com.example.abhyasa.security;

import com.example.abhyasa.jwt.JwtUtils;
import com.example.abhyasa.model.Token;
import com.example.abhyasa.repository.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;


@Service
@Data
public class LogoutService  implements LogoutHandler {


    @Autowired
    TokenRepo tokenRepo;


    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String jwt =jwtUtils.getJwtFromHeader(request);
        if(jwt==null)
             return;
        Token storedJwt = tokenRepo.findByToken(jwt).orElse(null);
        if(storedJwt!=null){
            storedJwt.setExpired(true);
            storedJwt.setRevoked(true);
            tokenRepo.save(storedJwt);
        }
    }
}
