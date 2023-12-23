package edu.sustech.cs209a.java2finalprojectdemo.service;

import edu.sustech.cs209a.java2finalprojectdemo.repository.AnswerRepository;
import edu.sustech.cs209a.java2finalprojectdemo.repository.CommentRepository;
import edu.sustech.cs209a.java2finalprojectdemo.repository.QuestionRepository;
import edu.sustech.cs209a.java2finalprojectdemo.repository.QuestionTagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RelateTopicService {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionTagsRepository questionTagsRepository;


    public HashMap<String,Integer> queryRelateTopic(String input){
        HashMap<String,Integer> relatedTopics = query(input);
        List<String> stringList = List.of(input.split("\\s+"));
        if (stringList.size()!=1) { //if not a word
            for (String str : stringList) {
                HashMap<String, Integer> map2 = query(str);
                map2.forEach((key, value) -> relatedTopics.merge(key, value, Integer::sum));
            }
        }
        relatedTopics.remove("java");
        relatedTopics.remove(input);
        relatedTopics.remove(input.toLowerCase());
        return relatedTopics;
    }

    public HashMap<String,Integer> query(String input){
        input = "%"+input+"%";
        HashMap<String,Integer> relatedTopics = new HashMap<>();
        List<Long> questions = questionRepository.findIdByBodyILike(input);
        addToList(relatedTopics,questions,5);
        List<Long> answers = answerRepository.findIdbybodyilike(input);
        addToList(relatedTopics,answers,1);
        return relatedTopics;
    }

    public void addToList(HashMap<String,Integer> targetList, List<Long> list, int weight) {
        for (Long i : list) {
            List<String> tmp = questionTagsRepository.findTagsByQuestionId(i);
            for (String str : tmp) {
                targetList.put(str,targetList.getOrDefault(str, 0) + weight);
            }
        }
    }

}
