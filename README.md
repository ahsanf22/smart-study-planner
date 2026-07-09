[![Maven CI](https://github.com/ahsanf22/smart-study-planner/actions/workflows/maven.yml/badge.svg)](https://github.com/ahsanf22/smart-study-planner/actions/workflows/maven.yml)
[![Coverage Status](https://coveralls.io/repos/github/ahsanf22/smart-study-planner/badge.svg)](https://coveralls.io/github/ahsanf22/smart-study-planner)

# Smart Study Planner

Smart Study Planner is a Spring Boot web application developed for the Automated Software Testing project.

The application allows a student to organize study categories and study tasks with priorities, deadlines, and completion status. The main focus of the project is not only the web application itself, but also the testing strategy, including unit tests, web-layer tests, integration tests, Testcontainers, code coverage, mutation testing, and CI quality checks.

## Main Features

- Dashboard with task and category summary
- Category management
- Task management
- Task priorities: LOW, MEDIUM, HIGH
- Task statuses: PENDING, COMPLETED
- Search and filter tasks
- Form validation
- Duplicate category handling
- Safe prevention of deleting categories that are still used by tasks

## Technology Stack

- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Data JPA
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- MockMvc
- Testcontainers
- JaCoCo
- PIT Mutation Testing
- GitHub Actions
- SonarQube Cloud workflow

## Architecture

The project follows a layered architecture:

```text
Controller layer
Service layer
Repository layer
Entity/domain layer
PostgreSQL database
```

## Testing Strategy

The project includes different levels of automated tests:

- Unit tests for service-layer business logic using Mockito
- Web-layer tests for Spring MVC controllers using MockMvc
- Repository integration tests using PostgreSQL Testcontainers
- Application context test
- JaCoCo code coverage report
- PIT mutation testing for checking the strength of the test suite

## Run Tests

```bash
./mvnw clean test
```

## Generate JaCoCo Coverage Report

```bash
./mvnw clean verify
```

The JaCoCo report is generated at:

```text
target/site/jacoco/index.html
```

## PIT Mutation Testing

The project includes PIT mutation testing for the service and domain layers.

Run mutation testing with:

```bash
./mvnw test-compile org.pitest:pitest-maven:mutationCoverage
```

The PIT report is generated at:

```text
target/pit-reports/index.html
```

Mutation testing is used to evaluate the strength of the test suite by checking whether tests can detect intentionally introduced code mutations.

## SonarQube Cloud

The project includes a manual GitHub Actions workflow for SonarQube Cloud analysis.

The workflow file is:

```text
.github/workflows/sonar.yml
```

To enable SonarQube Cloud analysis:

1. Import the GitHub repository into SonarQube Cloud.
2. Create a `SONAR_TOKEN`.
3. Add it to GitHub repository secrets as `SONAR_TOKEN`.
4. Run the `SonarQube Cloud Analysis` workflow manually from GitHub Actions.

SonarQube Cloud can analyze code quality, maintainability, reliability, security issues, and test coverage.

## Run Development Database

Start the PostgreSQL development database:

```bash
docker start studyplanner-db
```

If the database container does not exist, create it with:

```bash
docker run --name studyplanner-db \
  -e POSTGRES_DB=studyplanner \
  -e POSTGRES_USER=studyplanner \
  -e POSTGRES_PASSWORD=studyplanner \
  -p 5433:5432 \
  -d postgres:16-alpine
```

## Run Application

```bash
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8081/
```

## Important Pages

```text
/
 /categories
 /categories/new
 /tasks
 /tasks/new
```

## Project Purpose

This project demonstrates practical Automated Software Testing techniques in a realistic Spring Boot web application. It includes unit testing, integration testing with a real database environment, controller testing, validation testing, code coverage reporting, mutation testing, and automated build verification.

## Final Testing Stack

```text
JUnit 5
Mockito
MockMvc
PostgreSQL Testcontainers
JaCoCo coverage
PIT mutation testing
GitHub Actions CI
SonarQube Cloud workflow
```