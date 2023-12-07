package edu.sustech.cs209a.java2finalprojectdemo.repository;

import java.util.List;

public class StackOverflowApiResponse {
    private List<Question> items;

    public List<Question> getItems() {
        return items;
    }

    public void setItems(List<Question> items) {
        this.items = items;
    }
}
