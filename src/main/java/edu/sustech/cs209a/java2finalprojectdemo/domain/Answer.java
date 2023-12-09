package edu.sustech.cs209a.java2finalprojectdemo.domain;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

public class Answer {
    @JSONField(name = "answer_id")
    private Long id;
    private Long questionId;
    @JSONField(serialize = false,deserialize = false)
    private List<Object> owner;
    private boolean isAccepted;
    private String body;
    private Long score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public List<Object> getOwner() {
        return owner;
    }

    public void setOwner(List<Object> owner) {
        this.owner = owner;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
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
}
