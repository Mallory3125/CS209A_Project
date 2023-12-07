package edu.sustech.cs209a.java2finalprojectdemo.repository;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

public class Answer {
    private Long id;
    private Long questionId;
    @JSONField(serialize = false,deserialize = false)
    private List<Object> owner;
    private boolean isAccepted;
    private String body;
    private Long score;
}
