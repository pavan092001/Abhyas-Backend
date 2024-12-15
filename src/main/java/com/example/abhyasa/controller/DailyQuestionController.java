package com.example.abhyasa.controller;


import com.example.abhyasa.request.UpdateDQuestionReq;
import com.example.abhyasa.response.DQuesResponse;
import com.example.abhyasa.response.UpdateResponse;
import com.example.abhyasa.security.UserDetailsImpl;
import com.example.abhyasa.service.DailyQuestionService;
import com.example.abhyasa.service.MailService;
import com.example.abhyasa.service.OcrService;
import com.example.abhyasa.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DailyQuestionController {



    OcrService ocrService;




    @Autowired
    MailService mailService;
    private DailyQuestionService dailyQuestionService;

    private static final String UPLOAD_DIR = "uploads/";


    @Autowired
    public DailyQuestionController(@Autowired DailyQuestionService dailyQuestionService,@Autowired OcrService ocrService) {
        this.dailyQuestionService = dailyQuestionService;
        this.ocrService=ocrService;
    }

    @GetMapping("/getQuestion")
    public ResponseEntity<List<DQuesResponse>> getAssignQuestion(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("date") String date){

        List<DQuesResponse> list = dailyQuestionService.getQuestionDateWise(date, userDetails.getUid());
        return ResponseEntity.ok(list);
    }


    @PostMapping("/uploadQuestion")
    public  ResponseEntity<UpdateResponse> uploadImage(@RequestParam("file") MultipartFile file,@RequestParam("id") long id) throws IOException {
        UpdateResponse res=new UpdateResponse();
        System.out.println("testing");
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new UpdateResponse(false,"No file uploaded"));
        }
        MultipartFile file2= FileUtils.copyMultipartFile(file);
        String extractedText = ocrService.extractTextFromImage(file);
        System.out.println("extracted text "+extractedText);
       byte[] ph= file2.getBytes();
        if(extractedText.contains("Problem Solved Successfully")){
            System.out.println("In a Section ");
          //  byte[] photo= file.getBytes();
            res= dailyQuestionService.updateDailyQuestion(new UpdateDQuestionReq(id," ",ph));
        }
        return ResponseEntity.ok(res);
    }



}
