package com.example.abhyasa.repository;

import com.example.abhyasa.model.User;
import com.example.abhyasa.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    User findByEmail(String email);

}
