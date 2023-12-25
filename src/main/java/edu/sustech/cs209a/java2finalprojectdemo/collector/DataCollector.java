package edu.sustech.cs209a.java2finalprojectdemo.collector;
import com.alibaba.fastjson2.JSON;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import edu.sustech.cs209a.java2finalprojectdemo.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class DataCollector {
    static String key = "&key=o*hEYAOnnKscaF5L4szQYA((";
    //another key: o*hEYAOnnKscaF5L4szQYA((  24dXqAIrr2BSewLy3MHpkg((
    //each key has 10000 max quota

    //15 is the maximum number of each day
    static int pageSize = 50;
    static int start_page = 10;
    static int end_page = 11;

    static int cnt = 0;

    private static final Logger logger = LoggerFactory.getLogger(DataCollector.class);

    public static JSONArray loadFromUrl(String url){
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            //Content-Encoding:gzip
            InputStream responseBodyStream = new GZIPInputStream(response.body());
            // Content-Type:application/json; charset=utf-8
            String json = new String(responseBodyStream.readAllBytes(), StandardCharsets.UTF_8);
            //print the remain quota
//            System.out.println(json);
            cnt++;
            if (cnt % 100 == 0) System.out.println(json);
            JSONObject jsonObject = JSON.parseObject(json);
            logger.info("The returned JSONArray is {}.", jsonObject.getJSONArray("items"));
            return jsonObject.getJSONArray("items");
        } catch (Exception e) {
            logger.error("An " + e.getClass().getName() + "occurred during the operation");
            throw new RuntimeException(e);
        }
    }

    public static List<Question> collectQuestions() {
        logger.info("Starting to collect the questions");
        long startTime = System.nanoTime();
        String questionUrl = "https://api.stackexchange.com/2.3/search/advanced?page=%d&pagesize=%d&site=stackoverflow&filter=withbody&tagged=java&order=desc&sort=votes";
        questionUrl += key;
        List<Question> questions = new ArrayList<>();
        for (int i = start_page; i < end_page; i++) {
            JSONArray itemsArray = loadFromUrl(String.format(questionUrl, i+1, pageSize));
            addToList(itemsArray, Question.class, questions);
        }
        logger.info("Questions collection done");
        getTime(startTime);
        return questions;
    }

    public static List<Answer> collectAnswers(List<Question> questions) {
        logger.info("Starting to collect the answers");
        long startTime = System.nanoTime();
        String answerUrlTemplate = "https://api.stackexchange.com/2.3/questions/%d/answers?filter=withbody&order=desc&sort=votes&site=stackoverflow";
        List<Answer> answers = new ArrayList<>();
        answerUrlTemplate += key;
        for (Question question : questions) {
            JSONArray answerArray = loadFromUrl(String.format(answerUrlTemplate, question.getId()));
            addToList(answerArray, Answer.class, answers);
        }
        logger.info("Answers collection done");
        getTime(startTime);
        return answers;
    }

    public static List<Comment> collectCommentsFromQuestion(List<Question> questions) {
        logger.info("Starting to collect the comments from questions");
        long startTime = System.nanoTime();
        String questionUrlTemplate = "https://api.stackexchange.com/2.3/questions/%d/comments?order=desc&sort=votes&site=stackoverflow&filter=withbody";
        questionUrlTemplate += key;
        List<Comment> comments = new ArrayList<>();

        for (Question question : questions) {
            JSONArray commentArray = loadFromUrl(String.format(questionUrlTemplate, question.getId()));
            addToList(commentArray, Comment.class, comments);
        }
        logger.info("Question comments collection done");
        getTime(startTime);
        return comments;
    }
    public static List<Comment> collectCommentsFromAnswer(List<Answer> answers) {
        logger.info("Starting to collect the comments from answers");
        long startTime = System.nanoTime();
        String answerUrlTemplate = "https://api.stackexchange.com/2.3/answers/%d/comments?order=desc&sort=votes&site=stackoverflow&filter=withbody";
        answerUrlTemplate += key;
        List<Comment> comments = new ArrayList<>();

        for (Answer answer : answers) {
            JSONArray commentArray = loadFromUrl(String.format(answerUrlTemplate, answer.getId()));
            addToList(commentArray, Comment.class, comments);
        }
        logger.info("Answer comments collection done");
        getTime(startTime);
        return comments;
    }

    public static void addToList(JSONArray jsonArray, Class<?> targetClass, List<?> targetList) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Object targetObject = JSON.parseObject(jsonObject.toJSONString(), targetClass);
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) targetList;
            list.add(targetObject);
        }
    }


    public static void getTime(long startTime){
        long endTime = System.nanoTime();

        // Calculate execution time in nanoseconds
        long executionTime = endTime - startTime;

        // Convert execution time to milliseconds and print it
        logger.info("The execution time is " + executionTime / 1_000_000 + "milliseconds");
    }

}
