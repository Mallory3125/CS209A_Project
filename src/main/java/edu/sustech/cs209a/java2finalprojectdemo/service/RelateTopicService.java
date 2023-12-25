package edu.sustech.cs209a.java2finalprojectdemo.service;

import edu.sustech.cs209a.java2finalprojectdemo.repository.AnswerRepository;
import edu.sustech.cs209a.java2finalprojectdemo.repository.QuestionRepository;
import edu.sustech.cs209a.java2finalprojectdemo.repository.QuestionTagsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelateTopicService {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionTagsRepository questionTagsRepository;

    private static final Logger logger = LoggerFactory.getLogger(RelateTopicService.class);


    public List<Map.Entry<String, Double>> queryRelateTopic(String input){
        logger.info("Querying related topics for input: {}", input);

        HashMap<String,Integer> relatedTopics = query(input, 2);
        List<String> stringList = List.of(input.split("\\s+"));
        if (stringList.size() != 1) { //if not a word
            for (String str : stringList) {
                HashMap<String, Integer> map2 = query(str, 1);
                map2.forEach((key, value) -> relatedTopics.merge(key, value, Integer::sum));
            }
        }
        relatedTopics.remove("java");
        relatedTopics.remove(input);
        relatedTopics.remove(input.toLowerCase());

        int sum = relatedTopics.values().stream().mapToInt(Integer::intValue).sum();

        // 创建包含每个话题和它们百分比的列表
        List<Map.Entry<String, Double>> percentages = new ArrayList<>();
        relatedTopics.forEach((topic, count) -> {
            double percentage = 100.0 * count / sum;
            // 使用 SimpleEntry 而不是 HashMap
            Map.Entry<String, Double> topicPercentage = new AbstractMap.SimpleEntry<>(topic, percentage);
            percentages.add(topicPercentage);
        });
        percentages.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        logger.info("Related topics queried successfully");
        return percentages;
    }

    public HashMap<String,Integer> query(String str,int weight){
        logger.info("Performing query for input: {}", str);

        String input = "%" + str + "%";
        HashMap<String,Integer> relatedTopics = new HashMap<>();
        List<Long> questions = questionRepository.findIdByBodyILike(input);
        addToList(relatedTopics,questions, 5 * weight);
        List<Long> answers = answerRepository.findIdbybodyilike(input);
        addToList(relatedTopics,answers, weight);

        logger.info("Found {} questions and {} answers", questions.size(), answers.size());
        return relatedTopics;
    }

    public void addToList(HashMap<String,Integer> targetList, List<Long> list, int weight) {
        logger.info("Adding tags to targetList for list of size: {}", list.size());

        for (Long i : list) {
            List<String> tmp = questionTagsRepository.findTagsByQuestionId(i);
            for (String str : tmp) {
                targetList.put(str,targetList.getOrDefault(str, 0) + weight);
            }
        }

        logger.info(String.format("%d tags added to targetList", list.size()));
    }

}
