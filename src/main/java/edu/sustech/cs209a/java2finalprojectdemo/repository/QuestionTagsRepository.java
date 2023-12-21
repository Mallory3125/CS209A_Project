package edu.sustech.cs209a.java2finalprojectdemo.repository;

import edu.sustech.cs209a.java2finalprojectdemo.domain.QuestionTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTagsRepository extends JpaRepository<QuestionTags, Long> {
    @Query(value = "select tag_name from question_tags where question_tags.question_id = :questionId", nativeQuery = true)
    List<String> findTagsByQuestionId(Long questionId);
}
