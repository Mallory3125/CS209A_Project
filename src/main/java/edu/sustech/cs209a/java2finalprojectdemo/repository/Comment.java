package edu.sustech.cs209a.java2finalprojectdemo.repository;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

public class Comment {
    private Long id;
    private Long postId;
    @JSONField(serialize = false,deserialize = false)
    private List<Object> owner;
    private String body;
    private Long score;
}
