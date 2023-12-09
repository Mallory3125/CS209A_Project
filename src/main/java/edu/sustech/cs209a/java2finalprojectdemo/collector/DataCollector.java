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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
//&key=24dXqAIrr2BSewLy3MHpkg((
public class DataCollector {
    public static void main(String[] args) {
        String sampleUrl = "https://api.stackexchange.com/2.3/search/advanced?page=1&pagesize=10&order=desc&sort=votes&tagged=java&site=stackoverflow&filter=withbody";

        List<Question> questions = collectQuestions();
        List<Answer> answers = collectAnswers(questions);
        List<Comment> comments = collectComments(questions,answers);
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
            JSONObject jsonObject = JSON.parseObject(json);
            System.out.println(json);
            return jsonObject.getJSONArray("items");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Question> collectQuestions() {
        String questionUrl = "https://api.stackexchange.com/2.3/search/advanced?page=1&pagesize=50&site=stackoverflow&filter=withbody&tagged=java&order=desc&sort=votes";

        JSONArray itemsArray = loadFromUrl(questionUrl);
        List<Question> questions = new ArrayList<>();
        addToList(itemsArray, Question.class, questions);
        return questions;
    }

    public static List<Answer> collectAnswers(List<Question> questions) {
        String answerUrlTemplate = "https://api.stackexchange.com/2.3/questions/%d/answers?&filter=withbody&order=desc&sort=votes&site=stackoverflow";
        List<Answer> answers = new ArrayList<>();

        for (Question question : questions) {
            JSONArray answerArray = loadFromUrl(String.format(answerUrlTemplate, question.getId()));
            addToList(answerArray, Answer.class, answers);
        }

        return answers;
    }

    public static List<Comment> collectComments(List<Question> questions, List<Answer> answers) {
        String questionUrlTemplate = "https://api.stackexchange.com/2.3/questions/%d/comments?order=desc&sort=votes&site=stackoverflow";
        String answerUrlTemplate = "https://api.stackexchange.com/2.3/answers/%d/comments?order=desc&sort=votes&site=stackoverflow";

        List<Comment> comments = new ArrayList<>();

        for (Question question : questions) {
            JSONArray commentArray = loadFromUrl(String.format(questionUrlTemplate, question.getId()));
            addToList(commentArray, Comment.class, comments);
        }

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

}
