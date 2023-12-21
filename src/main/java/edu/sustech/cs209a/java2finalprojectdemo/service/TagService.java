package edu.sustech.cs209a.java2finalprojectdemo.service;

import edu.sustech.cs209a.java2finalprojectdemo.repository.QuestionTagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private QuestionTagsRepository questionTagsRepository;

    public List<String> getTags(Long questionId) {
        return questionTagsRepository.findTagsByQuestionId(questionId);
    }
}
