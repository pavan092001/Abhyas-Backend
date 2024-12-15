package com.example.abhyasa.service;

import com.example.abhyasa.model.DailyQuestion;
import com.example.abhyasa.model.Question;
import com.example.abhyasa.model.User;
import com.example.abhyasa.repository.DailyQuestionRepo;
import com.example.abhyasa.repository.QuestIonRepo; // Keeping the typo
import com.example.abhyasa.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyQuestionScheduler {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private QuestIonRepo questionRepository; // Keeping the typo

    @Autowired
    private DailyQuestionRepo dailyQuestionRepository;


    @Autowired
    MailService mailservice;







    public  void assignQuestion(User user){
        int noOfQuestion = user.getNoOfQuestion();
        List<DailyQuestion> list = new ArrayList<>();
        int totalAssigned = 0; // Track total assigned for this user
        for (int i = 0; i < noOfQuestion; i++) {
            Question nextQuestion = questionRepository.findNextQuestionsAfterId(i, PageRequest.of(0, 1)).stream().findFirst().orElse(null);

            if (nextQuestion != null) {
                DailyQuestion dailyQuestion = new DailyQuestion();
                dailyQuestion.setUser(user);
                dailyQuestion.setQuestion(nextQuestion);
                dailyQuestion.setAssignedDate(Date.valueOf(LocalDate.now()));
                dailyQuestion.setCompleted(false);

                dailyQuestionRepository.save(dailyQuestion);
                list.add(dailyQuestion);
                // Update the last assigned question ID
                 user.setLastAssignedQId(nextQuestion.getQId());
                //user.setLastAssignedQId(lastQuestionId); // Update user with the new ID
                totalAssigned++; // Increment the total assigned count
            } else {
                System.out.println("No more questions available for user: " + user.getEmail());
                break; // Exit if no more questions are found
            }
        }

        // Save the updated user with the new last assigned question ID
        userRepository.save(user);
        System.out.println("Assigned " + totalAssigned + " questions to user: " + user.getEmail());
        mailservice.sendMail(user.getName(), user.getEmail(),list);
        System.out.println("hello after sending mail");


    }


    @Scheduled(cron = "0 25 18 * * ?")
    public void assignDailyQuestions() {
        List<User> users = userRepository.findAll();
        System.out.println("Starting daily question assignment...");
        List<DailyQuestion> list;
        for (User user : users) {
            // Get the last assigned question ID for the user
            long lastQuestionId = user.getLastAssignedQId();
            int totalAssigned = 0; // Track total assigned for this user
            list= new ArrayList<>();
            // Fetch and assign the next 5 questions
            for (int i = 0; i < user.getNoOfQuestion(); i++) {
                // Fetch the next question after the last assigned question ID
                Question nextQuestion = questionRepository.findNextQuestionsAfterId(lastQuestionId, PageRequest.of(0, 1)).stream().findFirst().orElse(null);

                if (nextQuestion != null) {
                    DailyQuestion dailyQuestion = new DailyQuestion();
                    dailyQuestion.setUser(user);
                    dailyQuestion.setQuestion(nextQuestion);
                    dailyQuestion.setAssignedDate(Date.valueOf(LocalDate.now()));
                    dailyQuestion.setCompleted(false);

                    dailyQuestionRepository.save(dailyQuestion);
                    list.add(dailyQuestion);
                    // Update the last assigned question ID
                    lastQuestionId = nextQuestion.getQId();
                    user.setLastAssignedQId(lastQuestionId); // Update user with the new ID
                    totalAssigned++; // Increment the total assigned count
                } else {
                    System.out.println("No more questions available for user: " + user.getEmail());
                    break; // Exit if no more questions are found
                }
            }

            // Save the updated user with the new last assigned question ID
            userRepository.save(user);

            mailservice.sendMail(user.getName(),user.getEmail(),list);

            System.out.println("Assigned " + totalAssigned + " questions to user: " + user.getEmail());
        }

        System.out.println("Daily questions assigned successfully!");
    }








}
