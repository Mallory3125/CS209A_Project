package edu.sustech.cs209a.java2finalprojectdemo.service;

import edu.sustech.cs209a.java2finalprojectdemo.domain.Answer;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Comment;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Question;
import edu.sustech.cs209a.java2finalprojectdemo.helper.MyFatalError;
import edu.sustech.cs209a.java2finalprojectdemo.helper.MyRuntimeException;
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
    static HashMap<String,Integer> checkedExceptionsList = new HashMap<>();

    static HashMap<String,Integer> errorsList = new HashMap<>();
    static HashMap<String,Integer> fatalErrorList= new HashMap<>();
    static HashMap<String,Integer> otherErrorList= new HashMap<>();

    static List<Question> questions;
    static List<Answer> answers;
    static List<Comment> comments;

    @PostConstruct
    public void initial(){
        logger.info("Initialization of Error Service started");

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
        List<Map.Entry<String, Integer>> topNItems = fatalErrorList.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .toList();

        List<Map.Entry<String, Integer>> topNItem = otherErrorList.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .toList();

        classifyExceptions();

        logger.info(String.format("Initialization finished: %d kinds of errors , %d kinds of exceptions",errorsList.size(),exceptionsList.size()));
    }

    public HashMap<String,Integer> compareWithinCategory(String type){
        return switch (type) {
            case "runtime" -> runtimeExceptionsList;
            case "checked" -> checkedExceptionsList;
            case "fatal" -> fatalErrorList;
            case "other" -> otherErrorList;
            default -> null;
        };
    }

    public HashMap<String,Integer> compareBetweenCategory(){
        HashMap<String,Integer> map = new HashMap<>();
        map.put("runtimeExceptions",runtimeExceptionsList.values().stream().mapToInt(Integer::intValue).sum());
        map.put("checkedExceptions",checkedExceptionsList.values().stream().mapToInt(Integer::intValue).sum());
        map.put("fatalErrors",fatalErrorList.values().stream().mapToInt(Integer::intValue).sum());
        map.put("otherErrors",otherErrorList.values().stream().mapToInt(Integer::intValue).sum());
       return map;
    }

    public Integer queryBug(String str,String type){
        logger.info("Starting querying bugs");
        HashMap<String,Integer> map = new HashMap<>();
        if (type.equals("error")) map = errorsList;
        else if (type.equals("exception")) map = exceptionsList;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals(str)) {
                logger.info("Successfully completed querying bugs about " + str);
                return entry.getValue();
            }
        }
        logger.debug("No fitted bugs queried");
        return 0;
    }

    public List<Map.Entry<String, Integer>> queryTopN(int n,String type){
        HashMap<String,Integer> map = new HashMap<>();
        if (type.equals("error")) map = errorsList;
        else if (type.equals("exception")) map = exceptionsList;

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
            if (isKeyInEnum(entry.getKey(), MyRuntimeException.class, MyRuntimeException::getName)) {
                runtimeExceptionsList.put(entry.getKey(), entry.getValue());
            } else {
                checkedExceptionsList.put(entry.getKey(), entry.getValue());
            }
        }

        logger.info("Classification of exceptions completed");
    }

    public void classifyErrors(){
        for (Map.Entry<String, Integer> entry : errorsList.entrySet()) {
            if (isKeyInEnum(entry.getKey(), MyFatalError.class, MyFatalError::getName)) {
                fatalErrorList.put(entry.getKey(), entry.getValue());
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

    public static <T extends Enum<T>> boolean isKeyInEnum(
            String key,
            Class<T> enumClass,
            EnumNameProvider<T> nameProvider) {
        // Create a set of enum string representations
        Set<String> enumNames = EnumSet.allOf(enumClass).stream()
                .map(nameProvider::getName)
                .collect(Collectors.toSet());

        // Check if the key is in the set
        return enumNames.contains(key);
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
