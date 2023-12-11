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
//&key=24dXqAIrr2BSewLy3MHpkg((
public class DataCollector {
    static String key = "&key=24dXqAIrr2BSewLy3MHpkg((";

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
            JSONObject jsonObject = JSON.parseObject(json);
//            System.out.println(json);
            return jsonObject.getJSONArray("items");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Question> collectQuestions() {
        String questionUrl = "https://api.stackexchange.com/2.3/search/advanced?page=%d&pagesize=50&site=stackoverflow&filter=withbody&tagged=java&order=desc&sort=votes";
        questionUrl = questionUrl+key;
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JSONArray itemsArray = loadFromUrl(String.format(questionUrl, i+1));
            addToList(itemsArray, Question.class, questions);
        }
        return questions;
    }

    public static List<String> collectTags(List<Question> questions){
        Set<String> set = new HashSet<>();
        for (Question question : questions) {
            List<String> tmp = question.getTags();
            set.addAll(tmp);
        }
        return new ArrayList<>(set);
    }

    public static List<Answer> collectAnswers(List<Question> questions) {
        String answerUrlTemplate = "https://api.stackexchange.com/2.3/questions/%d/answers?filter=withbody&order=desc&sort=votes&site=stackoverflow";
        List<Answer> answers = new ArrayList<>();
        answerUrlTemplate+=key;
        for (Question question : questions) {
            JSONArray answerArray = loadFromUrl(String.format(answerUrlTemplate, question.getId()));
            addToList(answerArray, Answer.class, answers);
        }

        return answers;
    }

    public static List<Comment> collectComments(List<Question> questions, List<Answer> answers) {
        String questionUrlTemplate = "https://api.stackexchange.com/2.3/questions/%d/comments?order=desc&sort=votes&site=stackoverflow&filter=withbody";
        String answerUrlTemplate = "https://api.stackexchange.com/2.3/answers/%d/comments?order=desc&sort=votes&site=stackoverflow&filter=withbody";
        questionUrlTemplate+=key;
        answerUrlTemplate+=key;
        List<Comment> comments = new ArrayList<>();

        for (Question question : questions) {
            JSONArray commentArray = loadFromUrl(String.format(questionUrlTemplate, question.getId()));
            addToList(commentArray, Comment.class, comments);
        }
        System.out.println("question comment done");

        for (Answer answer : answers) {
            JSONArray commentArray = loadFromUrl(String.format(answerUrlTemplate, answer.getId()));
            addToList(commentArray, Comment.class, comments);
        }

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
