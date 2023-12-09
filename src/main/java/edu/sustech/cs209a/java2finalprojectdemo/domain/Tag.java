package edu.sustech.cs209a.java2finalprojectdemo.domain;


import com.alibaba.fastjson2.annotation.JSONField;

public class Tag {

//    private Long id;
@JSONField(serialize = false,deserialize = false)
    private String name;
    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
