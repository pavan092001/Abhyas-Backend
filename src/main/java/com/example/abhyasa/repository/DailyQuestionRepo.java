package com.example.abhyasa.repository;

import com.example.abhyasa.model.DailyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyQuestionRepo extends JpaRepository<DailyQuestion,Long> {

    //List<DailyQuestion> findByUserIdAndAssignedDate(UUID userId, LocalDate date);

    List<DailyQuestion> findByUserUidAndAssignedDate(Long uid, Date date);
}
