package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    QuestionService questionService;
    @GetMapping("/")
    public List<Map.Entry<String, Double>> getAllTopics(){
        return questionService.getRankingList();
    }

    @GetMapping("/top/{n}")
    public List<Map.Entry<String, Double>> getTopTopics(@PathVariable("n") int n) {
        return questionService.getTopKTopics(n);
    }

    @GetMapping("/heat")
    public Double getHeat(@RequestParam(value = "name") String str){
        return questionService.getHeatByTopic(str);
    }
}
