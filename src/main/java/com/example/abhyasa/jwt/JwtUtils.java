package com.example.abhyasa.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtils {



    @Value("${Spring.app.jwtSecret}")
    private  String jwtSecret;


    @Value("${Spring.app.jwtExpiration}")
    private  int jwtExpiration;



    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public  String generateJwtTokenFromEmail(String email,String role){
        return Jwts.builder()
                .setSubject(email)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+jwtExpiration))
                .signWith(key())
                .compact();
    }


    public  String getEmailFromJwt(String jwt) {
        return Jwts.parserBuilder().setSigningKey((SecretKey) key())
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    public String getJwtFromHeader(HttpServletRequest httpServletRequest){
        String bearer = httpServletRequest.getHeader("Authorization");
        if(bearer!=null && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }


    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parserBuilder().setSigningKey((SecretKey) key()).build().parseClaimsJws(authToken).getBody();
           // Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        }catch (MalformedJwtException e){
            System.out.println(e.getMessage());
        }catch (ExpiredJwtException e){
            System.out.println(e.getMessage());
        }catch (UnsupportedJwtException e){
            System.out.println(e.getMessage());
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

}
