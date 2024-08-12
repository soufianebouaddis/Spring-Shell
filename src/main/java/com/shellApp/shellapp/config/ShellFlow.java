package com.shellApp.shellapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;

@Component
public class ShellFlow {

    @Autowired
    private ComponentFlow.Builder componentFlowBuilder;

    public void runFlow() {
        ComponentFlow flow = componentFlowBuilder.clone().reset()
                .withSingleItemSelector("type")
                .name("Project Type")
                .selectItems(projectTypeOptions())
                .and()
                .withSingleItemSelector("packaging")
                .name("Packaging")
                .selectItems(packagingOptions())
                .and()
                .withSingleItemSelector("language")
                .name("Language")
                .selectItems(languageOptions())
                .and()
                .withStringInput("bootVersion")
                .name("Spring Boot Version")
                .defaultValue("3.1.0")
                .and()
                .withStringInput("groupId")
                .name("Group ID")
                .defaultValue("com.example")
                .and()
                .withStringInput("artifactId")
                .name("Artifact ID")
                .defaultValue("demo")
                .and()
                .withStringInput("name")
                .name("Project Name")
                .defaultValue("Demo Project")
                .and()
                .withStringInput("description")
                .name("Project Description")
                .defaultValue("A demo Spring Boot project")
                .and()
                .withStringInput("packageName")
                .name("Package Name")
                .defaultValue("com.example.demo")
                .and()
                .withStringInput("dependencies")
                .name("Dependencies (comma-separated)")
                .defaultValue("spring-boot-starter-web")
                .and()
                .build();

        ComponentFlow.ComponentFlowResult result = flow.run();
        displayResults(result);
    }

    // ... (projectTypeOptions, packagingOptions, and languageOptions methods remain the same)
    private Map<String, String> projectTypeOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("Maven", "maven");
        options.put("Gradle", "gradle");
        return options;
    }

    private Map<String, String> packagingOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("Jar", "jar");
        options.put("War", "war");
        return options;
    }

    private Map<String, String> languageOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("Java", "java");
        options.put("Kotlin", "kotlin");
        options.put("Groovy", "groovy");
        return options;
    }
    private void displayResults(ComponentFlow.ComponentFlowResult result) {
        System.out.println("Project Generation Options:");
        System.out.println("---------------------------");

        String[] expectedKeys = {"type", "packaging", "language", "bootVersion", "groupId", "artifactId", "name", "description", "packageName", "dependencies"};

        for (String key : expectedKeys) {
            try {
                Method method = result.getClass().getMethod("get" + capitalize(key));
                Object value = method.invoke(result);
                System.out.println(key + ": " + (value != null ? value : "Not provided"));
            } catch (Exception e) {
                System.out.println(key + ": Unable to retrieve (Error: " + e.getMessage() + ")");
            }
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
