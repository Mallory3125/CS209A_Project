package edu.sustech.cs209a.java2finalprojectdemo.collector;
import com.alibaba.fastjson2.JSON;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import edu.sustech.cs209a.java2finalprojectdemo.repository.*;

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
        String sampleurl = "https://api.stackexchange.com/2.3/search/advanced?page=1&pagesize=10&order=desc&sort=votes&tagged=java&site=stackoverflow&filter=withbody";

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
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            return itemsArray;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Question> collectQuestions(){
        String qUrl = "https://api.stackexchange.com/2.3/search/advanced?page=10&pagesize=50&site=stackoverflow&filter=withbody&tagged=java&order=desc&sort=votes";
        JSONArray itemsArray = loadFromUrl(qUrl);
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < itemsArray.size(); i++) {
            JSONObject questionObject = itemsArray.getJSONObject(i);
            Question question = JSON.parseObject(questionObject.toJSONString(), Question.class);
            questions.add(question);
        }
        return questions;
        // TODO: 12/5/2023
    }


    public static List<Answer> collectAnswers(List<Question> questions){
        String aUrl = "https://api.stackexchange.com/2.3/questions/{id}/answers?&filter=withbody&order=desc&sort=votes&site=stackoverflow";

        List<Answer> answers = new ArrayList<>();
        for (Question question: questions) {
            Long question_id = question.getId();
        }
        return answers;
        // TODO: 12/5/2023
    }

    public static List<Comment> collectComments(List<Question> questions,List<Answer> answers){
        String cUrl = "https://api.stackexchange.com/2.3/questions/{id}/comments?order=desc&sort=votes&site=stackoverflow";
        String cUrl2 = "https://api.stackexchange.com/2.3/answers/{id}/comments?order=desc&sort=votes&site=stackoverflow";
        List<Comment> comments = new ArrayList<>();
        for (Question question: questions) {
            Long question_id = question.getId();
        }
        for (Answer answer: answers) {
//            Long answer_id = answer.getId();
        }
        return comments;
    }
}
