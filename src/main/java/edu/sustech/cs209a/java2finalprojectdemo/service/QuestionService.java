package edu.sustech.cs209a.java2finalprojectdemo.service;

import edu.sustech.cs209a.java2finalprojectdemo.domain.Question;
import edu.sustech.cs209a.java2finalprojectdemo.helper.Topic;
import edu.sustech.cs209a.java2finalprojectdemo.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagService tagService;

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private static List<Map.Entry<String, Double>> sortedList;

    public List<Question> findAllQuestions() {
        return questionRepository.findAllQuestions();
    }

    public List<Map.Entry<String, Double>> getRankingList() {
        logger.info("Starting the method getRankingList in Question Service");

        if (sortedList != null) {
            logger.info("Sorted ranking list of topics already exists");
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
                    ranking.put(currentTopic, question.getAnswerCount() +
                        question.getScore() / 1e1 + question.getViewCount() / 1e3 + ranking.get(currentTopic));


                } catch (Exception e) {
                    logger.error("A " + e.getClass().getName() + " occurred when generalizing the top topics");
                }
            }
        }

        sortedList = new ArrayList<>(ranking.entrySet());
        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        logger.info("Ranking list generated");
        return sortedList;
    }

    public List<Map.Entry<String, Double>> getTopKTopics(int k) {
        List<Map.Entry<String, Double>> topTopics = new ArrayList<>();

        if(k > 10 || k < 1) {
            logger.warn("The given number of top topics to be searched exceeds the program range, which is " + k);
        }

        try {
            for (int i = 0; i < Math.min(k, 10); i++) {
                if (i >= sortedList.size()) {
                    break;
                }

                Map.Entry<String, Double> entry = sortedList.get(i);
                topTopics.add(entry);
            }
            logger.info("Returned top {} topics", topTopics.size());
        } catch (NullPointerException e) {
            logger.error(String.format("A " + e.getClass().getName() +
                "occurred during the operation when getting the top %d topics, which is caused because the initialization of top topics " +
                "has not been finished", k));
            try {
                logger.info("Starting the initialization of ranking the top topics");
                this.getRankingList();
                logger.info("Initialization of ranking the top topics has been finished");
            } catch (Exception exception) {
                logger.error("During fixing the " + e.getClass().getName() + ", another " + exception.getClass().getName() + "occurred");
            }
        } catch (Exception e) {
            logger.error(String.format("A " + e.getClass().getName() + " occurred during getting the top %d topics", k));
        }

        return topTopics;
    }

    public Double getHeatByTopic(String givenTopic) {
        try {
            for (Map.Entry<String, Double> entry : sortedList) {
                String topic = entry.getKey();
                Double heat = entry.getValue();
                if (Objects.equals(topic, givenTopic)) {
                    logger.info("Heat of topic '{}' found: {}", givenTopic, heat);
                    return heat;
                }
            }
        } catch (NullPointerException e) {
            logger.error(String.format("A " + e.getClass().getName() +
                "occurred during the operation when getting the heat of" + givenTopic + ", which is caused " +
                "because the initialization of top topics has not been finished"));
            try {
                logger.info("Starting the initialization of ranking the top topics");
                this.getRankingList();
                logger.info("Initialization of ranking the top topics has been finished");
            } catch (Exception exception) {
                logger.error("During fixing the " + e.getClass().getName() + ", another " + exception.getClass().getName() + "occurred");
            }
        } catch (Exception e) {
            logger.error("A " + e.getClass().getName() + " occurred when getting the heat of the topic " + givenTopic);
        }

        logger.info("An exception occurred then returned 0");
        return 0.0;
    }
}
