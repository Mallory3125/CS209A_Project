package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.service.RelateTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/related")
public class RelatedTopicController {
    @Autowired
    RelateTopicService relateTopicService;

    @GetMapping("/search")
    public List<Map.Entry<String, Double>> searchRelatedTopics(@RequestParam(value = "topic") String topic){
        return relateTopicService.queryRelateTopic(topic);
    }
}
