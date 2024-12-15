package com.example.abhyasa.service;

import com.example.abhyasa.model.DailyQuestion;
import com.example.abhyasa.repository.DailyQuestionRepo;
import com.example.abhyasa.request.UpdateDQuestionReq;
import com.example.abhyasa.response.DQuesResponse;
import com.example.abhyasa.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;




@Service
public class DailyQuestionServiceImpl implements DailyQuestionService {







    DailyQuestionRepo repo;




    @Value("${backend.url}")
    private  String backendUrl;


    @Autowired
    public DailyQuestionServiceImpl(DailyQuestionRepo repo) {
                this.repo = repo;
    }

    @Override
    public List<DQuesResponse> getQuestionDateWise(String date, long uid) {
        Date d = Date.valueOf(date);
        List<DailyQuestion>  listQuestion=repo.findByUserUidAndAssignedDate(uid,d);
        List<DQuesResponse>  dlist = listQuestion.stream().map(que->new DQuesResponse(que.getDQId(),que.getQuestion(),que.getAssignedDate(),que.isCompleted(), que.getPhoto())).toList();
        return dlist;
    }

    @Override
    public UpdateResponse updateDailyQuestion(UpdateDQuestionReq req) {
        DailyQuestion question = repo.findById(req.getDQid()).get();
        question.setCompleted(true);
        question.setPhoto(req.getPhoto());
        DailyQuestion saved2= repo.save(question);
        if(saved2!=null)
            return new UpdateResponse(true,"Update Success ");
        return new UpdateResponse(false,"Update Failed");

    }
}
