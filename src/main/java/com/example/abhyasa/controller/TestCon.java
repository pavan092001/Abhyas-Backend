package com.example.abhyasa.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCon {









    @GetMapping("/testing")
    public String ge(){
        return "Testing";
    }
}
