package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.service.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Controller
@RequestMapping("/bug")
public class BugController {
    @Autowired
    ErrorService questionService;

    @GetMapping("/")
    public void test(){
        questionService.initial();
    }

    @GetMapping("/comparewithin")
    public HashMap<String,Integer> compareWithin(@RequestParam(value = "type") String type){
        return questionService.compareWithinCategory(type);
    }

    @GetMapping("/comparebetween")
    public HashMap<String,Integer> compareBetween(){
        return questionService.compareBetweenCategory();
    }
    @GetMapping("/querybug")
    public Integer queryBug(@RequestParam(value = "name") String name,
                            @RequestParam(value = "type") String type){
        return questionService.queryBug(name, type);
    }

    @GetMapping("/gettopn")
    public List<Map.Entry<String, Integer>> queryTopN(@RequestParam(value = "n") int n,
                                                      @RequestParam(value = "type") String type){
        return questionService.queryTopN(n, type);
    }


//
//    @GetMapping({"/", "/demoPage"})
//    public String demoPage() {
//        return "demoPage";
//    }
}
