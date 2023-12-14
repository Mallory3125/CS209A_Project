package edu.sustech.cs209a.java2finalprojectdemo.DAO;

import edu.sustech.cs209a.java2finalprojectdemo.domain.Question;
import org.hibernate.boot.model.internal.PropertyInferredData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    private static final String DB_URL = "jdbc:postgresql://" + "localhost" + "/" + "cs209a";
    private static final String DB_USER = "checker";
    private static final String DB_PASSWORD = "114514";

    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = con.createStatement()) {

            String query = "SELECT * FROM questions";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String body = rs.getString("body");
                Long score = rs.getLong("score");
                Long viewCount = rs.getLong("view_count");
                Long answerCount = rs.getLong("answer_count");

                Question question = new Question();
                question.setId(id);
                question.setTitle(title);
                question.setBody(body);
                question.setScore(score);
                question.setViewCount(viewCount);
                question.setAnswerCount(answerCount);

                questions.add(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }
}

