package edu.sustech.cs209a.java2finalprojectdemo.domain;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class Question {
    @JSONField(name = "question_id")
    private Long id;
    private Long tagId;
    @JSONField(serialize = false)
    private List<Tag> tags;
    @JSONField(serialize = false,deserialize = false)
    private List<Object> owner;
    private String title;
    private String body;
    private Long score;

    private Long viewCount;
    private Long answerCount;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +

                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", score=" + score +
                ", viewCount=" + viewCount +
                ", answerCount=" + answerCount +
                '}';
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = new ArrayList<>();
        for (String s:tags) {
            this.tags.add(new Tag(s));
        }

    }

    public Question() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Long answerCount) {
        this.answerCount = answerCount;
    }
}
