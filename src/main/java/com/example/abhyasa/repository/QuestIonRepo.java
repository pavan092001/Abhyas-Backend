package com.example.abhyasa.repository;

import com.example.abhyasa.model.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestIonRepo extends JpaRepository<Question,Long> {
//    @Query("SELECT q FROM Question q WHERE q.qId > :lastQuestionId ORDER BY q.qId ASC")
//    Question findNextQuestionAfterId(@Param("lastQuestionId") long lastQuestionId);



    @Query(value = "SELECT q FROM Question q WHERE q.qId > :lastQuestionId ORDER BY q.qId ASC")
    List<Question> findNextQuestionsAfterId(@Param("lastQuestionId") long lastQuestionId, Pageable pageable);
}
