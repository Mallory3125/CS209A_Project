package edu.sustech.cs209a.java2finalprojectdemo.domain;


import com.alibaba.fastjson2.annotation.JSONField;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    private Long id;
    @JSONField(serialize = false,deserialize = false)
    private String name;
    public Tag(String name) {
        this.name = name;
    }

    public Tag() {    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
