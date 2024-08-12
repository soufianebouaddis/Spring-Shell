package com.shellApp.shellapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ShellService {

    private static final Logger logger = LoggerFactory.getLogger(ShellService.class);
    //private final WebClient webClient;
    private final RestTemplate restTemplate;
    public ShellService(/*WebClient.Builder webClientBuilder,*/ RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

/*
    public void generateProject(String type, String packaging,String language, String bootVersion, String baseDir, String groupId,
                                String artifactId, String name, String description, String packageName, String dependencies) throws IOException {

        logger.info("Starting project generation...");

        */
/*Mono<InputStreamResource> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/starter.zip")
                        .queryParam("type", type)
                        .queryParam("language", language)
                        .queryParam("bootVersion", bootVersion)
                        .queryParam("baseDir", baseDir)
                        .queryParam("groupId", groupId)
                        .queryParam("artifactId", artifactId)
                        .queryParam("name", name)
                        .queryParam("description", description)
                        .queryParam("packageName", packageName)
                        .queryParam("dependencies", dependencies)
                        .build())
                .retrieve()
                .bodyToMono(InputStreamResource.class);

        InputStream inputStream = response.block().getInputStream();
        FileOutputStream outputStream = new FileOutputStream(baseDir + ".zip");*//*

        String url = "https://start.spring.io/#!type" + type +"&packaging="+packaging+ "&language=" + language + "&bootVersion=" + bootVersion +
                "&baseDir=" + baseDir + "&groupId=" + groupId + "&artifactId=" + artifactId + "&name=" + name +
                "&description=" + description + "&packageName=" + packageName + "&dependencies=" + dependencies;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Resource> response = restTemplate.exchange(url, HttpMethod.GET, entity, Resource.class);

        InputStream inputStream = response.getBody().getInputStream();
        FileOutputStream outputStream = new FileOutputStream(baseDir + ".zip");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();

        logger.info("Project downloaded successfully as {}.zip", baseDir);
    }
*/
public void generateProject(String type, String packaging, String language, String bootVersion, String baseDir,
                            String groupId, String artifactId, String name, String description, String packageName,
                            String dependencies) throws IOException {
    logger.info("Starting project generation...");

    String url = "https://start.spring.io/starter.zip";
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("type", encode(type))
            .queryParam("packaging", encode(packaging))
            .queryParam("language", encode(language))
            .queryParam("bootVersion", encode(bootVersion))
            .queryParam("baseDir", encode(baseDir))
            .queryParam("groupId", encode(groupId))
            .queryParam("artifactId", encode(artifactId))
            .queryParam("name", encode(name))
            .queryParam("description", encode(description))
            .queryParam("packageName", encode(packageName))
            .queryParam("dependencies", encode(dependencies));

    String finalUrl = builder.build(true).toUriString();
    logger.info("Request URL: " + finalUrl);

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.ACCEPT, "application/zip");
    headers.set(HttpHeaders.USER_AGENT, "SpringProjectGenerator");

    HttpEntity<String> entity = new HttpEntity<>(headers);

    try {
        ResponseEntity<byte[]> response = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                entity,
                byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] zipContent = response.getBody();
            String filename = extractFilename(response.getHeaders());
            try (FileOutputStream outputStream = new FileOutputStream(filename)) {
                outputStream.write(zipContent);
            }
            logger.info("Project downloaded successfully as " + filename);
        } else {
            logger.info("Failed to download project. Status code: " + response.getStatusCode());
        }
    } catch (Exception e) {
        logger.info("Error generating project: " + e.getMessage());
        throw new IOException("Failed to generate project", e);
    }
}

    private String extractFilename(HttpHeaders headers) {
        String contentDisposition = headers.getFirst(HttpHeaders.CONTENT_DISPOSITION);
        if (contentDisposition != null && contentDisposition.contains("filename=")) {
            String filename = contentDisposition.split("filename=")[1].replace("\"", "");
            return filename.endsWith(".zip") ? filename : filename + ".zip";
        }
        return "demo.zip"; // Default filename
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
public String genHelp() {
        return "Usage: Gen [OPTIONS]\n\n" +
                "Generate a new Spring Boot project with specified options.\n\n" +
                "Options:\n" +
                "  --type              Project Type: 'maven-project' or 'gradle-project'\n" +
                "  --language          Programming Language: 'java', 'kotlin', or 'groovy'\n" +
                "  --boot-version      Spring Boot Version (e.g., '2.5.0', '2.6.3')\n" +
                "  --base-dir          Base Directory Name for the project\n" +
                "  --group-id          Group ID in the format 'com.example'\n" +
                "  --artifact-id       Artifact ID: name of the project artifact\n" +
                "  --name              Project Name: human-readable name of the project\n" +
                "  --description       Project Description: brief summary of the project\n" +
                "  --package-name      Package Name: main package for source code (e.g., 'com.example.project')\n" +
                "  --dependencies      Dependencies: comma-separated list (e.g., 'web,data-jpa,security')\n\n" +
                "Example:\n" +
                "  Gen --type maven-project --language java --boot-version 2.5.0 --base-dir myproject \\\n" +
                "      --group-id com.example --artifact-id demo --name \"Demo Project\" \\\n" +
                "      --description \"A demo Spring Boot project\" --package-name com.example.demo \\\n" +
                "      --dependencies web,data-jpa,security";
    }
}
