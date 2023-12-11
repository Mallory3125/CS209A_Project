package edu.sustech.cs209a.java2finalprojectdemo.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "id")
    @JSONField(name = "comment_id")
    private Long id;
    @Column(name = "post_id")
    @JSONField(name = "post_id")
    private Long postId;

    private String body;
    private Long score;
    @JSONField(serialize = false,deserialize = false)
    @Transient
    private List<Object> owner;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", body='" + body + '\'' +
                ", score=" + score +
                ", owner=" + owner +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public List<Object> getOwner() {
        return owner;
    }

    public void setOwner(List<Object> owner) {
        this.owner = owner;
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
