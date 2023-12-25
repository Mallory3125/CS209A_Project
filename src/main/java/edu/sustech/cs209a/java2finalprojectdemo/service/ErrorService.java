package edu.sustech.cs209a.java2finalprojectdemo.service;

import edu.sustech.cs209a.java2finalprojectdemo.domain.Answer;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Comment;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Question;
import edu.sustech.cs209a.java2finalprojectdemo.helper.*;
import edu.sustech.cs209a.java2finalprojectdemo.repository.AnswerRepository;
import edu.sustech.cs209a.java2finalprojectdemo.repository.CommentRepository;
import edu.sustech.cs209a.java2finalprojectdemo.repository.QuestionRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ErrorService {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    CommentRepository commentRepository;

    private static final Logger logger = LoggerFactory.getLogger(ErrorService.class);
    static HashMap<String,Integer> exceptionsList = new HashMap<>();
    static HashMap<String,Integer> runtimeExceptionsList = new HashMap<>();
    static HashMap<String,Integer> ioExceptionsList = new HashMap<>();
    static HashMap<String,Integer> otherExceptionsList= new HashMap<>();

    static HashMap<String,Integer> errorsList = new HashMap<>();
    static HashMap<String,Integer> linkageErrorList= new HashMap<>();
    static HashMap<String,Integer> VMErrorList= new HashMap<>();
    static HashMap<String,Integer> otherErrorList= new HashMap<>();

    static List<Question> questions;
    static List<Answer> answers;
    static List<Comment> comments;

    @PostConstruct
    public void initial(){
        logger.info("Initialization of Error Service started");

        try {
            questions = questionRepository.findQuestionAboutBug();
            answers = answerRepository.findAnswerAboutBug();
            comments = commentRepository.findCommentAboutBug();

            for (Question q : questions) {
                List<String> list = extractErrorNames(q.getBody(),"Error");
                addToList(errorsList,list, Math.toIntExact(q.getViewCount()/50000));
                List<String> list2 = extractErrorNames(q.getBody(),"Exception");
                addToList(exceptionsList,list2,Math.toIntExact(q.getViewCount()/50000));
            }
            for (Answer a : answers) {
                List<String> list = extractErrorNames(a.getBody(),"Error");
                addToList(errorsList,list,1);
                List<String> list2 = extractErrorNames(a.getBody(),"Exception");
                addToList(exceptionsList,list2,1);
            }
            for (Comment c : comments) {
                List<String> list = extractErrorNames(c.getBody(),"Error");
                addToList(errorsList,list,1);
                List<String> list2 = extractErrorNames(c.getBody(),"Exception");
                addToList(exceptionsList,list2,1);
            }
            classifyErrors();
            classifyExceptions();
        } catch (Exception e) {
            logger.error("A " + e.getClass().getName() + " occurred during the operation");
        }

        logger.info(String.format("Initialization finished: %d kinds of errors , %d kinds of exceptions", errorsList.size(), exceptionsList.size()));
    }

    public HashMap<String,Integer> compareWithinCategory(String type){
        HashMap<String,Integer> map;
        switch (type) {
            case "RuntimeException" -> map = runtimeExceptionsList;
            case "IOException" -> map = ioExceptionsList;
            case "otherException" -> map = otherExceptionsList;
            case "VirtualMachineError" -> map = VMErrorList;
            case "LinkageError" -> map = linkageErrorList;
            case "otherError" -> map = otherErrorList;
            default -> map = new HashMap<>();
        }
        if (map.isEmpty()){
            logger.warn("The given type in method compareWithinCategory does not match, the returned map would be null");
        } else {
            logger.info("Comparing within Categories finished");
        }
        return map;
    }

    public HashMap<String,Integer> compareBetweenCategory(String type){
        HashMap<String,Integer> map = new HashMap<>();
        if(type.equals("error")){
            map.put("VirtualMachineError",VMErrorList.values().stream().mapToInt(Integer::intValue).sum());
            map.put("LinkageError",linkageErrorList.values().stream().mapToInt(Integer::intValue).sum());
            map.put("otherError",otherErrorList.values().stream().mapToInt(Integer::intValue).sum());
            logger.info("Comparing between categories finished");
        }
        else if (type.equals("exception")) {
            map.put("RuntimeException", runtimeExceptionsList.values().stream().mapToInt(Integer::intValue).sum());
            map.put("IOException", ioExceptionsList.values().stream().mapToInt(Integer::intValue).sum());
            map.put("otherException", otherExceptionsList.values().stream().mapToInt(Integer::intValue).sum());
            logger.info("Comparing between categories finished");
        }
        else {
            logger.warn("The given type in method compareBetweenCategory does not match, the returned map would be null");
        }
       return map;
    }

    public Integer queryBug(String str,String type){
        logger.info("Starting querying bugs");
        HashMap<String,Integer> map;
        if (type.equals("error")) map = errorsList;
        else if (type.equals("exception")) map = exceptionsList;
        else {
            logger.warn("The given type in method queryBug does not match, the returned number would be 0");
            return 0;
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals(str)) {
                logger.info("Successfully completed querying bug about " + str);
                return entry.getValue();
            }
        }
        logger.warn("No fitted bugs found");
        return 0;
    }

    public List<Map.Entry<String, Integer>> queryTopN(int n,String type){
        HashMap<String,Integer> map;
        if (type.equals("error")) map = errorsList;
        else if (type.equals("exception")) map = exceptionsList;
        else {
            logger.warn("The given type does not match, the returned map would be null");
            return null;
        }

        logger.info(String.format("Querying top %d %s:", n, type));
        List<Map.Entry<String, Integer>> topNItems = map.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(n)
            .collect(Collectors.toList());

        logger.info(String.format("Querying for top %d %s completed", n, type));
        return topNItems;
    }

    public void classifyExceptions(){
        for (Map.Entry<String, Integer> entry : exceptionsList.entrySet()) {
            MyRuntimeException runtimeException = isKeyInEnum(entry.getKey(), MyRuntimeException.class, MyRuntimeException::getName);
            if (runtimeException != null) {
                runtimeExceptionsList.put(runtimeException.getName(), entry.getValue());
                continue;
            }
            MyIOException ioException = isKeyInEnum(entry.getKey(), MyIOException.class, MyIOException::getName);
            if(ioException != null){
                ioExceptionsList.put(ioException.getName(),entry.getValue());
            }
            else {
                otherExceptionsList.put(entry.getKey(), entry.getValue());
            }
        }
        logger.info("Classification of exceptions completed");
    }

    public void classifyErrors(){
        for (Map.Entry<String, Integer> entry : errorsList.entrySet()) {
            MyLinkageError linkageError = isKeyInEnum(entry.getKey(), MyLinkageError.class, MyLinkageError::getName);
            if (linkageError != null) {
                linkageErrorList.put(linkageError.getName(), entry.getValue());
                continue;
            }
            MyVirtualMachineError virtualMachineError = isKeyInEnum(entry.getKey(), MyVirtualMachineError.class, MyVirtualMachineError::getName);
            if (virtualMachineError != null) {
                VMErrorList.put(virtualMachineError.getName(), entry.getValue());
            } else {
                otherErrorList.put(entry.getKey(), entry.getValue());
            }
        }
        logger.info("Classification of errors completed");
    }

    public static void addToList( HashMap<String,Integer> targetList, List<String> list, int weight) {
        for (String str : list) {
            targetList.put(str,targetList.getOrDefault(str, 0) + weight);
        }
    }

    public static String formatName(String fullExceptionName) {
        int lastDotIndex = fullExceptionName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            // If there's a period, return the substring after the last period.
            return fullExceptionName.substring(lastDotIndex + 1);
        } else {
            // If there's no period, the fullExceptionName does not contain any package parts, return as is.
            return fullExceptionName;
        }
    }

    public static <T extends Enum<T>> T isKeyInEnum(
            String key,
            Class<T> enumClass,
            EnumNameProvider<T> nameProvider) {
        Optional<T> matchedEnum = EnumSet.allOf(enumClass).stream()
                .filter(e -> nameProvider.getName(e).equalsIgnoreCase(key))
                .findFirst();

        // 如果找到枚举值则返回，否则返回 null 或者你可以选择抛出异常
        return matchedEnum.orElse(null);
    }
    @FunctionalInterface
    public interface EnumNameProvider<T extends Enum<T>> {
        String getName(T enumConstant);
    }
    public static List<String> extractErrorNames(String text,String type) {
        // Regular expression pattern for fully qualified classname
        final String patternString = "\\b([a-zA-Z_][\\w\\.]*[\\w])"+type+"\\b";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(patternString);

        // Create a matcher object
        Matcher matcher = pattern.matcher(text);

        // A Set to hold all unique matches
        Set<String> matches = new HashSet<>();

        // Loop through and find all matches
        while (matcher.find()) {
            // Add each matched group to the list
            matches.add(formatName(matcher.group()));
//            matches.add(matcher.group());
        }

        // Return the list of matches (error names)
        return new ArrayList<>(matches);
    }
}
