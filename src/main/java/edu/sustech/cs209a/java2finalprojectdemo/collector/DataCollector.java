package edu.sustech.cs209a.java2finalprojectdemo.collector;
import com.alibaba.fastjson2.JSON;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import edu.sustech.cs209a.java2finalprojectdemo.domain.*;

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
    static int pageSize = 2;
    static int start_page = 8;
    static int end_page = 10;

    static int cnt = 0;

    public static void main(String[] args) {
        collectQuestions();
    }

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
            System.out.println(json);
            cnt++;
            if (cnt%100==0) System.out.println(json);
            JSONObject jsonObject = JSON.parseObject(json);
            return jsonObject.getJSONArray("items");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Question> collectQuestions() {
        System.out.println("start collect questions");
        long startTime = System.nanoTime();
        String questionUrl = "https://api.stackexchange.com/2.3/search/advanced?page=%d&pagesize=%d&site=stackoverflow&filter=withbody&tagged=java&order=desc&sort=votes";
        questionUrl += key;
        List<Question> questions = new ArrayList<>();
        for (int i = start_page; i < end_page; i++) {
            JSONArray itemsArray = loadFromUrl(String.format(questionUrl, i+1, pageSize));
            addToList(itemsArray, Question.class, questions);
        }
        System.out.println("Questions collection done.");
        getTime(startTime);
        return questions;
    }

    public static List<Answer> collectAnswers(List<Question> questions) {
        System.out.println("start collect answers");
        long startTime = System.nanoTime();
        String answerUrlTemplate = "https://api.stackexchange.com/2.3/questions/%d/answers?filter=withbody&order=desc&sort=votes&site=stackoverflow";
        List<Answer> answers = new ArrayList<>();
        answerUrlTemplate += key;
        for (Question question : questions) {
            JSONArray answerArray = loadFromUrl(String.format(answerUrlTemplate, question.getId()));
            addToList(answerArray, Answer.class, answers);
        }
        System.out.println("Answers collection done.");
        getTime(startTime);
        return answers;
    }

    public static List<Comment> collectCommentsFromQuestion(List<Question> questions) {
        System.out.println("start collect comments");
        long startTime = System.nanoTime();
        String questionUrlTemplate = "https://api.stackexchange.com/2.3/questions/%d/comments?order=desc&sort=votes&site=stackoverflow&filter=withbody";
        questionUrlTemplate += key;
        List<Comment> comments = new ArrayList<>();

        for (Question question : questions) {
            JSONArray commentArray = loadFromUrl(String.format(questionUrlTemplate, question.getId()));
            addToList(commentArray, Comment.class, comments);
        }
        System.out.println("Question comments collection done.");
        getTime(startTime);
        return comments;
    }
    public static List<Comment> collectCommentsFromAnswer(List<Answer> answers) {
        System.out.println("start collect comments");
        long startTime = System.nanoTime();
        String answerUrlTemplate = "https://api.stackexchange.com/2.3/answers/%d/comments?order=desc&sort=votes&site=stackoverflow&filter=withbody";
        answerUrlTemplate += key;
        List<Comment> comments = new ArrayList<>();

        for (Answer answer : answers) {
            JSONArray commentArray = loadFromUrl(String.format(answerUrlTemplate, answer.getId()));
            addToList(commentArray, Comment.class, comments);
        }
        System.out.println("Answer comments collection done.");
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
        System.out.println("Execution time: " + executionTime / 1_000_000 + " milliseconds");
    }

}
