package edu.sustech.cs209a.java2finalprojectdemo.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @Column(name = "id")
    @JSONField(name = "answer_id")
    private Long id;
    @Column(name = "question_id")
    @JSONField(name = "question_id")
    private Long questionId;

    @Column(name = "is_accepted")
    @JSONField(name = "is_accepted")
    private boolean accepted;
    private String body;
    private Long score;
    @JSONField(serialize = false,deserialize = false)
    @Transient
    private List<Object> owner;
    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", accepted=" + accepted +
                ", score=" + score +
                '}';
    }

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

    public boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
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
