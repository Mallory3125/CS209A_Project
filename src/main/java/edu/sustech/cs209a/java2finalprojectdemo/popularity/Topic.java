package edu.sustech.cs209a.java2finalprojectdemo.popularity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Topic {

    Map<String, List<String>> topics = new HashMap<>();

    public Topic() {
        topics.put("Performance", List.of(
            "performance",
            "cpu-architecture",
            "branch-prediction",
            "memory",
            "memory-leaks",
            "incompatibility",
            "unsupported-class-version",
            "optimization"
        ));

        topics.put("Parameter Passing", List.of(
            "parameter-passing",
            "pass-by-reference",
            "pass-by-value",
            "default-parameters",
            "parameters"
        ));

        topics.put("Data Structure", List.of(
            "collections",
            "hashmap",
            "hashtable",
            "dictionary",
            "arraylist",
            "linked-list"
        ));

        topics.put("Android", List.of(
            "android",
            "usermanager",
            "android-networking",
            "networkonmainthread",
            "http",
            "httprequest",
            "httpurlconnection",
            "urlconnection"
        ));

        topics.put("Multithreading", List.of(
            "multithreading",
            "java-threads",
            "runnable",
            "implements",
            "dependency-injection"
        ));

        topics.put("Exception", List.of(
            "nullpointerexception",
            "exception"
        ));

        topics.put("Testing and Assertion", List.of(
            "junit",
            "junit4",
            "assert"
        ));

        topics.put("Reflection and Annotation", List.of(
            "reflection",
            "annotations"
        ));

        topics.put("I/O", List.of(
            "file",
            "file-io",
            "io",
            "java-io"
        ));

        topics.put("Tool", List.of(
            "build",
            "maven-2",
            "executable-jar",
            "build-automation",
            "mvn-repo",
            "apache-camel",
            "soa",
            "enterprise-integration",
            "maven",
            "dependencies",
            "libraries"
        ));
    }

    public String getTopic(String tag) {
        for (Map.Entry<String, List<String>> entry : topics.entrySet()) {
            String topic = entry.getKey();
            List<String> tags = entry.getValue();

            if (tags.contains(tag)) {
                return topic;
            }
        }

        return "";
    }

//    public static void main(String[] args) {
//        Topic topic = new Topic();
//        System.out.println(topic.getTopic("maven"));
//        System.out.println(topic.getTopic("c++"));
//    }

}
