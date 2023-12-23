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
@Controller
@RequestMapping("/bug")
public class BugController {
    @Autowired
    ErrorService errorService;
//
//    @GetMapping("/")
//    public void test(){
//        questionService.initial();
//    }

    @GetMapping("/comparewithin")
    public HashMap<String,Integer> compareWithin(@RequestParam(value = "type") String type){
        System.out.println("comarewithin");
        return errorService.compareWithinCategory(type);
    }

    @GetMapping("/comparebetween")
    public void compareBetween(HttpServletResponse response) throws IOException {
        System.out.println("comparebetween");
        HashMap<String,Integer> map = errorService.compareBetweenCategory();
        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(map));
    }
    @GetMapping("/querybug")
    public Integer queryBug(@RequestParam(value = "name") String name,
                            @RequestParam(value = "type") String type){
        System.out.println("querybug");
        return errorService.queryBug(name, type);
    }

    @GetMapping("/gettopn")
    public List<Map.Entry<String, Integer>> queryTopN(@RequestParam(value = "n") int n,
                                                      @RequestParam(value = "type") String type){
        System.out.println("gettopn");
        return errorService.queryTopN(n, type);
    }


//
//    @GetMapping({"/", "/demoPage"})
//    public String demoPage() {
//        return "demoPage";
//    }
}
