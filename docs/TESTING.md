# Testing Strategy

This project demonstrates automated software testing using multiple testing levels and quality tools.

The goal of the testing strategy is to verify business logic, controller behavior, persistence behavior, and build reproducibility.

## Testing Levels

The project uses the following testing levels:

```text
Unit tests
Web-layer tests
Repository integration tests
Application context test
Coverage analysis
Mutation testing
Continuous integration
```

## Unit Tests

Service-layer business logic is tested using JUnit 5 and Mockito.

Tested examples:

- Creating categories
- Rejecting duplicate category names
- Rejecting blank category names
- Creating study tasks
- Rejecting tasks with missing categories
- Rejecting blank task titles
- Marking tasks as completed
- Searching tasks
- Preventing deletion of categories used by tasks

Main service test classes:

- `CategoryServiceImplTest`
- `StudyTaskServiceImplTest`

## Web-Layer Tests

Spring MVC controllers are tested using MockMvc.

These tests verify controller behavior without starting the full web server.

Tested examples:

- Correct view names
- Correct model attributes
- Form validation errors
- Redirect behavior
- Duplicate category handling
- Category deletion warning messages
- Task creation
- Task status changes

Main controller test classes:

- `HomeControllerTest`
- `CategoryControllerTest`
- `StudyTaskControllerTest`

## Repository Integration Tests

Repository integration tests use PostgreSQL Testcontainers.

This means the tests run against a real PostgreSQL database instead of an in-memory database.

Tested repositories:

- `CategoryRepository`
- `StudyTaskRepository`

Testcontainers provides a temporary PostgreSQL container during the test execution. This improves realism and reproducibility.

## Application Context Test

The project includes a Spring Boot context loading test:

- `SmartStudyPlannerApplicationTests`

This verifies that the Spring application context starts correctly with the configured beans.

## JaCoCo Code Coverage

JaCoCo is configured to generate a code coverage report.

Run:

```bash
./mvnw clean verify
```

Report location:

```text
target/site/jacoco/index.html
```

The JaCoCo report helps evaluate which parts of the source code are covered by automated tests.

## PIT Mutation Testing

PIT mutation testing is configured to evaluate the effectiveness of the test suite.

Run:

```bash
./mvnw test-compile org.pitest:pitest-maven:mutationCoverage
```

Report location:

```text
target/pit-reports/index.html
```

Mutation testing checks whether tests can detect small artificial changes in the production code. This provides a stronger indication of test quality than line coverage alone.

## Continuous Integration

GitHub Actions is configured to run the Maven build and automated tests.

Workflow file:

```text
.github/workflows/maven.yml
```

The CI workflow runs on pushes and pull requests to the main branch.

## SonarQube Cloud Workflow

A manual SonarQube Cloud workflow is included for code quality analysis.

Workflow file:

```text
.github/workflows/sonar.yml
```

The workflow requires a GitHub repository secret named:

```text
SONAR_TOKEN
```

SonarQube Cloud can analyze maintainability, reliability, code smells, duplication, and coverage data.

## Test Execution Commands

Run all standard tests:

```bash
./mvnw clean test
```

Run tests and generate coverage:

```bash
./mvnw clean verify
```

Run mutation testing:

```bash
./mvnw test-compile org.pitest:pitest-maven:mutationCoverage
```

## Summary

The project includes:

- JUnit 5 tests
- Mockito unit tests
- MockMvc web-layer tests
- PostgreSQL Testcontainers integration tests
- JaCoCo coverage reporting
- PIT mutation testing
- GitHub Actions CI
- SonarQube Cloud workflow configuration
