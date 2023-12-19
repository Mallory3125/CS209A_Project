package edu.sustech.cs209a.java2finalprojectdemo.popularity;

import edu.sustech.cs209a.java2finalprojectdemo.DAO.QuestionDAO;
import edu.sustech.cs209a.java2finalprojectdemo.DAO.TagDAO;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Question;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Tag;
import edu.sustech.cs209a.java2finalprojectdemo.visualization.PieChartDrawer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.util.*;

public class TopicRanker {
    static Topic topic;
    static Map<String, Double> ranking = new HashMap<>();
    static List<Map.Entry<String, Double>> sortedList;


    public static List<Map.Entry<String, Double>> getRankingList() {
        if (sortedList != null) {
            return sortedList;
        }

        QuestionDAO questionDAO = new QuestionDAO();
        TagDAO tagDAO = new TagDAO();

        topic = new Topic();

        List<String> initialKeys = Arrays.asList(
            "Performance", "Tool", "Parameter Passing", "I/O", "Data Structure",
            "Android", "Multithreading", "Exception", "Testing and Assertion",
            "Reflection and Annotation"
        );

        for (String key : initialKeys) {
            ranking.putIfAbsent(key, 0.0);
        }

        List<Question> questions = questionDAO.getQuestions();

        for (Question question : questions) {
            for (String tag : tagDAO.getTags(question.getId())) {
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

//        PieChartDrawer.drawPieChart(sortedList);

    }

    public static List<Map.Entry<String, Double>> getTopKTopics(int k) {
        List<Map.Entry<String, Double>> sortedList = getRankingList();
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

    public static Double getHeatByTopic(String givenTopic) {
        List<Map.Entry<String, Double>> sortedList = getRankingList();

        for (Map.Entry<String, Double> entry : sortedList) {
            String topic = entry.getKey();
            Double heat = entry.getValue();

            if (Objects.equals(topic, givenTopic)) {
                return heat;
            }

        }

        return 0.0;
    }

    public static void main(String[] args) {
        List<Map.Entry<String, Double>> list = getTopKTopics(3);
        for (Map.Entry<String, Double> entry : list) {
            String topic = entry.getKey();
            Double heat = entry.getValue();

            System.out.println("Topic: " + topic + ", Heat: " + heat);
        }

        System.out.println();

        String topic = "I/O";

        System.out.println("Topic: " + topic + ", Heat: " + getHeatByTopic(topic));

    }


}
