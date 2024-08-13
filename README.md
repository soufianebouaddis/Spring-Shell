# Spring Boot Project Generator CLI

This is a simple CLI tool for generating Spring Boot projects using Spring Initializr. The tool allows you to specify various parameters such as project type, packaging, language, dependencies, and more, and automatically downloads a pre-configured project as a ZIP file.

## Features

- Generate a new Spring Boot project with customizable settings.
- Supports Maven,gradle as the build system.
- Specify dependencies, group ID, artifact ID, and more directly from the command line.
- Automatically downloads the project as a ZIP file.

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher

## Usage

### help and gen Command Example

To display all available commands and their descriptions, use the following command:

```bash
help
gen maven-project jar java 3.3.0 demo com.example my-app My-Application spring-project com.example.demo web,devtools,data-jpa
mvn spring-boot:run

