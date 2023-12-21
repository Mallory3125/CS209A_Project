package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.popularity.TopicRanker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Controller
@RequestMapping("/topic")
public class TopicController {

    @GetMapping("/")
    public List<Map.Entry<String, Double>> test(){
        return TopicRanker.getRankingList();
    }

    @GetMapping("/querytopn")
    public List<Map.Entry<String, Double>> test2(@RequestParam(value = "name") int n){
        return TopicRanker.getTopKTopics(n);
    }

    @GetMapping("/queryheat")
    public Double test3(@RequestParam(value = "name") String str){
        return TopicRanker.getHeatByTopic(str);
    }
}
