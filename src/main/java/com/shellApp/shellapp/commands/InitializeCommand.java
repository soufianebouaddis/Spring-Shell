package com.shellApp.shellapp.commands;
import com.shellApp.shellapp.config.ShellFlow;
import com.shellApp.shellapp.service.ShellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;

@ShellComponent
public class InitializeCommand {

    private final ShellService shellService;
    @Autowired
    private ShellFlow projectGeneratorFlow;
    @Autowired
    public InitializeCommand(ShellService springInitializrService) {
        this.shellService = springInitializrService;
    }


    @ShellMethod(key = "gen-flow", value = "Generate a new Spring Boot project by flow")
    public void generateProject() {
        projectGeneratorFlow.runFlow();
    }
    @ShellMethod(key="gen",value = "Generate your spring project easly using this shell")
    public void generateProject(
            @ShellOption(help = "Project Type (e.g., maven-project, gradle-project)") String type,
            @ShellOption(help = "Packaging Type (e.g., jar, war)") String packaging,
            @ShellOption(help = "Language (e.g., java, kotlin, groovy)") String language,
            @ShellOption(help = "Spring Boot Version") String bootVersion,
            @ShellOption(help = "Base Directory Name") String baseDir,
            @ShellOption(help = "Group ID") String groupId,
            @ShellOption(help = "Artifact ID") String artifactId,
            @ShellOption(help = "Project Name") String name,
            @ShellOption(help = "Project Description") String description,
            @ShellOption(help = "Package Name") String packageName,
            @ShellOption(help = "Dependencies (comma-separated, e.g., web,security)") String dependencies) {
        try {
            shellService.generateProject(type, packaging,language, bootVersion, baseDir, groupId, artifactId, name, description, packageName, dependencies);
        } catch (IOException e) {
            System.err.println("Error generating project: " + e.getMessage());
        }
    }
    @ShellMethod(key="gen-help",value = "How to use gen command")
    public String genCommandHelp(){
        return shellService.genHelp();
    }
}
