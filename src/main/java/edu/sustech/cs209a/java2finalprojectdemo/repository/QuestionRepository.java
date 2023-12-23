package edu.sustech.cs209a.java2finalprojectdemo.repository;

import edu.sustech.cs209a.java2finalprojectdemo.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Question searchQuestionById(Long id);
    @Query(value = "select id from questions where body ILIKE :str ", nativeQuery = true)
    List<Long> findIdByBodyILike(String str);

    @Query(value = "select * from questions where body ILIKE '%error%' or body ILIKE  '%exception%'", nativeQuery = true)
    List<Question> findQuestionAboutBug();

    @Query(value = "select * from questions", nativeQuery = true)
    List<Question> findAllQuestions();

}
