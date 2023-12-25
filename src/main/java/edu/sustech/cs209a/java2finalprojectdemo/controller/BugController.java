package edu.sustech.cs209a.java2finalprojectdemo.controller;

import com.alibaba.fastjson2.JSON;
import edu.sustech.cs209a.java2finalprojectdemo.service.ErrorService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bug")
public class BugController {
    @Autowired
    ErrorService errorService;

    @GetMapping("/compare/within-category")
    public Map<String, Integer> compareWithinCategory(@RequestParam(value = "type") String type) {
        return errorService.compareWithinCategory(type);
    }

    @GetMapping("/compare/between-categories")
    public Map<String, Integer> compareBetweenCategories(@RequestParam(value = "type") String type) {
        return errorService.compareBetweenCategory(type);
    }

    @GetMapping("/heat")
    public Integer queryBugCount(@RequestParam(value = "name") String name,
                                 @RequestParam(value = "type") String type) {
        return errorService.queryBug(name, type);
    }

    @GetMapping("/top")
    public List<Map.Entry<String, Integer>> queryTopErrors(@RequestParam(value = "n") int n,
                                                           @RequestParam(value = "type") String type) {
        return errorService.queryTopN(n, type);
    }
//
//    @GetMapping({"/", "/demoPage"})
//    public String demoPage() {
//        return "demoPage";
//    }
}
