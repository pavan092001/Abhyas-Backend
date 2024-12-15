package com.example.abhyasa.service;

import com.example.abhyasa.request.UpdateDQuestionReq;
import com.example.abhyasa.response.DQuesResponse;
import com.example.abhyasa.response.UpdateResponse;

import java.util.List;

public interface DailyQuestionService {




    List<DQuesResponse> getQuestionDateWise(String date, long uid);

    public UpdateResponse updateDailyQuestion(UpdateDQuestionReq req);



}
