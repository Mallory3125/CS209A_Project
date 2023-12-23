package edu.sustech.cs209a.java2finalprojectdemo.repository;

import edu.sustech.cs209a.java2finalprojectdemo.domain.Answer;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Comment;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query(value ="select question_id from answers where body ILIKE :str",nativeQuery = true)
    List<Long> findIdbybodyilike(String str);

    @Query(value ="select * from answers where body ILIKE '%error%' or body ILIKE  '%exception%'",nativeQuery = true)
    List<Answer> findAnswerAboutBug();
}
