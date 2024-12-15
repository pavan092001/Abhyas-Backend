package com.example.abhyasa.controller;


import com.example.abhyasa.dto.UserDto;
import com.example.abhyasa.model.User;
import com.example.abhyasa.request.LoginRequest;
import com.example.abhyasa.request.SignUpRequest;
import com.example.abhyasa.response.AuthResponse;
import com.example.abhyasa.service.DailyQuestionScheduler;
import com.example.abhyasa.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {



    @Autowired
    UserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;



    @GetMapping("/csrf-token")
    CsrfToken csrf(HttpServletRequest req){
        return (CsrfToken) req.getAttribute(CsrfToken.class.getName());
    }


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse>  signup( @RequestBody SignUpRequest req){
        System.out.println("signup "+req);
        AuthResponse authResponse = userService.signUp(req);
        return ResponseEntity.ok(authResponse);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(HttpServletRequest request,@RequestBody LoginRequest req){
        AuthResponse authResponse = userService.signIn(authenticationManager,req);
        String csrf = csrf(request).getToken();
        authResponse.setCsrfToken(csrf);
        return ResponseEntity.ok(authResponse);
    }





//    @PostMapping("/signup")
//    public String signup(@RequestBody UserDto userDto){
//       // User u = userService.signIn(userDto);
//      //  return (u==null)?"False":"True";
//    }
}
