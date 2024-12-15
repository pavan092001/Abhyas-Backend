package com.example.abhyasa.service;


import com.example.abhyasa.model.DailyQuestion;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Data
public class MailService {



    @Value("${frontend.url}")
    private String loginUrl;



    @Value("${backend.url")
    private  String backendUrl;

    @Autowired
    JavaMailSender javaMailSender;


    public void sendMail(String name,String to, List<DailyQuestion> list){
       try{
           MimeMessage message = javaMailSender.createMimeMessage();
           MimeMessageHelper messageHelper= new MimeMessageHelper(message,true);
           messageHelper.setTo(to);
           messageHelper.setSubject("Your today's Coding Task from Abhyasa ");
           StringBuilder htmlContent = new StringBuilder();
           htmlContent.append("<html><body>");
           String imgurl= "email_bg.png";
           htmlContent.append("<p><img src='").append("https://img.freepik.com/free-vector/hand-drawn-web-developers_23-2148819604.jpg").append("' alt='Abhyasa Logo' style='width:75%;'/></p>");
           htmlContent.append("<h1>Hello ").append(name).append(" </h1>");
           htmlContent.append("<p>Here are your daily coding questions:</p>");
           htmlContent.append("<ul>");
           // Append questions
           for (DailyQuestion question : list) {
               htmlContent.append("<li>").append(question.getQuestion().getTitle()).append("</li>");
           }
           htmlContent.append("</ul>");
           htmlContent.append("<p>Submit your answers here: <a href='").append(loginUrl).append("'>Login</a></p>");
           htmlContent.append("<p>Happy Coding!<br>The Abhyasa Team</p>");
           htmlContent.append("</body></html>");

           messageHelper.setText(htmlContent.toString(),true);
           File imageFile = new File("src/main/java/com/example/abhyasa/service/email_bg.png"); // Update the path if necessary
           messageHelper.addInline("emailImage", imageFile);

           javaMailSender.send(message);
           System.out.println("Email sent successfully to: " + to);

       }catch (MessagingException e){
           e.printStackTrace();
           System.err.println("Error sending email: " + e.getMessage());
       }

    }
 }
