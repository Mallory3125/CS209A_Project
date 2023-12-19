package edu.sustech.cs209a.java2finalprojectdemo.repository;

import edu.sustech.cs209a.java2finalprojectdemo.domain.Comment;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    @Query(value ="select * from comments where body ILIKE :str",nativeQuery = true)
    List<Comment> findbybodyilike(String str);

    @Query(value ="select * from comments where body ILIKE '%error%' or body ILIKE  '%exception%'",nativeQuery = true)
    List<Comment> findCommentAboutBug();
}
