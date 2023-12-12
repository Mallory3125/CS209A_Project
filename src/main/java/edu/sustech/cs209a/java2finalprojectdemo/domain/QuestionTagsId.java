package edu.sustech.cs209a.java2finalprojectdemo.domain;

import java.io.Serializable;
import java.util.Objects;

public class QuestionTagsId implements Serializable {

    private String tagName;
    private Long questionId;

    public QuestionTagsId() {
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionTagsId that = (QuestionTagsId) o;
        return questionId == that.questionId && Objects.equals(tagName, that.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, questionId);
    }
}
