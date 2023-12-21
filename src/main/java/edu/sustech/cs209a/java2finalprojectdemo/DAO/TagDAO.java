package edu.sustech.cs209a.java2finalprojectdemo.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {
    private static final String DB_URL = "jdbc:postgresql://" + "localhost" + "/" + "cs209a";
    private static final String DB_USER = "checker";
    private static final String DB_PASSWORD = "123456";

    public List<String> getTags(Long questionId) {
        List<String> tags = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = con.prepareStatement(
                 "SELECT tag_name FROM question_tags WHERE question_id = ?"
             )) {

            stmt.setLong(1, questionId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tagName = rs.getString("tag_name");
                tags.add(tagName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tags;
    }

    public static void main(String[] args) {
        TagDAO tagDAO = new TagDAO();
        System.out.println(tagDAO.getTags(11227809L));
        System.out.println(tagDAO.getTags(1L));
    }
}

