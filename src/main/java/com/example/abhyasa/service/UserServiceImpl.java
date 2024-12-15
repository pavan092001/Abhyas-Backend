package com.example.abhyasa.service;

import com.example.abhyasa.dto.UserDto;
import com.example.abhyasa.jwt.JwtUtils;
import com.example.abhyasa.model.Token;
import com.example.abhyasa.model.User;
import com.example.abhyasa.repository.TokenRepo;
import com.example.abhyasa.repository.UserRepo;
import com.example.abhyasa.request.LoginRequest;
import com.example.abhyasa.request.SignUpRequest;
import com.example.abhyasa.response.AuthResponse;
import com.example.abhyasa.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements  UserService {


    @Autowired
    UserRepo userRepo;



    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    BCryptPasswordEncoder encoder;


    @Autowired
    TokenRepo tokenRepo;

//    public User signIn(UserDto dto){
//        User user = new User();
//        user.setEmail(dto.getEmail());
//        user.setPassword(dto.getPassword());
//        return userRepo.save(user);
//
//    }



    @Autowired
    DailyQuestionScheduler dailyQuestionScheduler;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        return UserDetailsImpl.build(user);
    }

    @Override
    public AuthResponse signUp(SignUpRequest sign) {
        User user = new User();
        user.setEmail(sign.getEmail());
        user.setName(sign.getName());
       // System.out.println("password"+sign.getPassword());
        user.setPassword(encoder.encode(sign.getPassword()));
        user.setEnabled(false);
        user.setEnabled(true);
        user.setRole(sign.getRole());

        user.setLastAssignedQId(0L);
        User u2 =userRepo.save(user);
        AuthResponse authResponse = new AuthResponse();
        if(u2!=null){
            authResponse.setStatus(true);
            authResponse.setRole(user.getRole());
            String jwt = jwtUtils.generateJwtTokenFromEmail(user.getEmail(), user.getRole());
            authResponse.setUser(new UserDto(user.getName(),user.getEmail(),user.getRole(),user.isTwoFactorEnabled(),user.getCreatedDate(),user.getUpdatedDate()));
            authResponse.setMessage("Successfully SignUp");
            authResponse.setJwt(jwt);
            revokeAllUserToken(user);
            saveToken(jwt, user);
            dailyQuestionScheduler.assignQuestion(user);
        }else{
            authResponse.setMessage("Failed to create Account");
            authResponse.setUser(null);
            authResponse.setRole(null);
            authResponse.setStatus(false);
        }
        return authResponse;
    }

    private void saveToken(String jwt, User user) {
        Token token = Token.builder().token(jwt).tokenType("Bearer ").user(user).expired(false).revoked(false).build();
        tokenRepo.save(token);
    }

    @Override
    public AuthResponse signIn(AuthenticationManager authenticationManager, LoginRequest req) {
        Authentication authentication = null;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(),req.getPassword()));
        }catch (Exception e){
            e.printStackTrace();
        }

        if (authentication == null) {
            throw new RuntimeException("Authentication failed");
        }
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt =jwtUtils.generateJwtTokenFromEmail(userDetails.getEmail(),userDetails.getRole());
        List<String> roles = List.of(userDetails.getRole());
        UserDto userDto = new UserDto(userDetails.getName(),userDetails.getEmail(),userDetails.getRole(),userDetails.is2faEnabled(),userDetails.getCreatedDate(),userDetails.getUpdatedDate());
        AuthResponse response = new AuthResponse();
        response.setMessage("Login Successfully");
        response.setRole(userDto.getRole());
        response.setUser(userDto);
        response.setJwt(jwt);
        response.setStatus(true);
        User user = userRepo.findById(userDetails.getUid()).get();
        revokeAllUserToken(user);
        saveToken(jwt,user);
        //System.out.println("response "+response);
        return response;
    }


    private void revokeAllUserToken(User user){
        List<Token> tokens = tokenRepo.findAllValidTokenByUid(user.getUid());
        if(tokens.isEmpty())
             return;
        tokens.forEach(t->{
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepo.saveAll(tokens);
    }



}
