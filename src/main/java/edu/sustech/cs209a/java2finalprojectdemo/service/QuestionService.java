package edu.sustech.cs209a.java2finalprojectdemo.service;

import edu.sustech.cs209a.java2finalprojectdemo.domain.Question;
import edu.sustech.cs209a.java2finalprojectdemo.popularity.Topic;
import edu.sustech.cs209a.java2finalprojectdemo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagService tagService;

    private static List<Map.Entry<String, Double>> sortedList;

    public List<Question> findAllQuestions() {
        return questionRepository.findAllQuestions();
    }

    public List<Map.Entry<String, Double>> getRankingList() {
        if (sortedList != null) {
            return sortedList;
        }

        Map<String, Double> ranking = new HashMap<>();
        Topic topic = new Topic();
        List<String> initialKeys = Arrays.asList(
            "Performance", "Tool", "Parameter Passing", "I/O", "Data Structure",
            "Android", "Multithreading", "Exception", "Testing and Assertion",
            "Reflection and Annotation"
        );

        for (String key : initialKeys) {
            ranking.putIfAbsent(key, 0.0);
        }

        List<Question> questions = this.findAllQuestions();

        for (Question question : questions) {

            List<String> tags = tagService.getTags(question.getId());

            for (String tag : tags) {
                try {
                    String currentTopic = topic.getTopic(tag);
                    if (Objects.equals(currentTopic, "")) {
                        continue;
                    }

                    ranking.putIfAbsent(currentTopic, 0.0);
                    ranking.put(currentTopic, question.getAnswerCount() + question.getScore() / 1e1 + question.getViewCount() / 1e3 + ranking.get(currentTopic));

                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }

        sortedList = new ArrayList<>(ranking.entrySet());
        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        return sortedList;
    }

    public List<Map.Entry<String, Double>> getTopKTopics(int k) {
        List<Map.Entry<String, Double>> topTopics = new ArrayList<>();

        for (int i = 0; i < Math.min(k, 10); i++) {
            if (i >= sortedList.size()) {
                break;
            }

            Map.Entry<String, Double> entry = sortedList.get(i);
            topTopics.add(entry);
        }

        return topTopics;
    }

    public Double getHeatByTopic(String givenTopic) {
        for (Map.Entry<String, Double> entry : sortedList) {
            String topic = entry.getKey();
            Double heat = entry.getValue();
            if (Objects.equals(topic, givenTopic)) {
                return heat;
            }
        }
        return 0.0;
    }
}
