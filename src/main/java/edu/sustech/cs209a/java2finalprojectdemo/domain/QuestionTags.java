package edu.sustech.cs209a.java2finalprojectdemo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "question_tags")
@IdClass(QuestionTagsId.class)
public class QuestionTags {

    @Id
    @Column(name = "tag_name")
    private String tagName;

    @Id
    @Column(name = "question_id")
    private Long questionId;

    public QuestionTags() {
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
