package com.example.abhyasa.repository;

import com.example.abhyasa.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TokenRepo extends JpaRepository<Token,Long> {





    @Query("select  t from Token  t inner  join User  u on t.user.uid=u.uid  where u.uid=:userId and (t.expired=false  or t.revoked=false)")
    public List<Token> findAllValidTokenByUid(Long userId);





    Optional<Token> findByToken(String token);


}
