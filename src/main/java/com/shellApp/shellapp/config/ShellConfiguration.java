package com.shellApp.shellapp.config;

import com.shellApp.shellapp.service.ShellService;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ShellConfiguration {

    private final ShellService shellService;

    public ShellConfiguration(ShellService shellService) {
        this.shellService = shellService;
    }

    @Bean
    public CommandRegistration generateProjectCommand() {
        return CommandRegistration.builder()
                .command("generate-project")
                .withOption()
                .longNames("type")
                .description("Project Type (e.g., maven-project, gradle-project)")
                .required()
                .and()
                .withOption()
                .longNames("packaging")
                .description("Packaging type (jar,war)")
                .required()
                .and()
                .withOption()
                .longNames("language")
                .description("Language (e.g., java, kotlin, groovy)")
                .required()
                .and()
                .withOption()
                .longNames("boot-version")
                .description("Spring Boot Version")
                .required()
                .and()
                .withOption()
                .longNames("base-dir")
                .description("Base Directory Name")
                .required()
                .and()
                .withOption()
                .longNames("group-id")
                .description("Group ID")
                .required()
                .and()
                .withOption()
                .longNames("artifact-id")
                .description("Artifact ID")
                .required()
                .and()
                .withOption()
                .longNames("name")
                .description("Project Name")
                .required()
                .and()
                .withOption()
                .longNames("description")
                .description("Project Description")
                .required()
                .and()
                .withOption()
                .longNames("package-name")
                .description("Package Name")
                .required()
                .and()
                .withOption()
                .longNames("dependencies")
                .description("Dependencies (comma-separated, e.g., web,security)")
                .required()
                .and()
                .withTarget()
                .function(ctx -> {
                    try {
                        shellService.generateProject(
                                ctx.getOptionValue("type"),
                                ctx.getOptionValue("packaging"),
                                ctx.getOptionValue("language"),
                                ctx.getOptionValue("boot-version"),
                                ctx.getOptionValue("base-dir"),
                                ctx.getOptionValue("group-id"),
                                ctx.getOptionValue("artifact-id"),
                                ctx.getOptionValue("name"),
                                ctx.getOptionValue("description"),
                                ctx.getOptionValue("package-name"),
                                ctx.getOptionValue("dependencies")
                        );
                        return "Project generated successfully.";
                    } catch (IOException e) {
                        return "Error generating project: " + e.getMessage();
                    }
                })
                .and()
                .build();
    }
}
