package edu.sustech.cs209a.java2finalprojectdemo.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @Column(name = "id")
    @JSONField(name = "question_id")
    private Long id;

    private String title;
    private String body;
    private Long score;
    @Column(name = "view_count")
    @JSONField(name = "view_count")
    private Long viewCount;
    @Column(name = "answer_count")
    @JSONField(name = "answer_count")
    private Long answerCount;
    @JSONField(serialize = false)
    @Transient
    private List<String> tags;
    @JSONField(serialize = false,deserialize = false)
    @Transient
    private List<Object> owner;
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", tagSize=" + (tags != null ? tags.size() : 0) +
                ", owner=" + owner +
                ", title='" + title + '\'' +
                ", score=" + score +
                ", viewCount=" + viewCount +
                ", answerCount=" + answerCount +
                '}';
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
//    public List<Tag> getTags() {
//        return tags;
//    }
//
//    public void setTags(List<String> tags) {
//        this.tags = new ArrayList<>();
//        for (String s:tags) {
//            this.tags.add(new Tag(s));
//        }
//    }

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
