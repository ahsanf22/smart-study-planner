# Smart Study Planner

Smart Study Planner is a Spring Boot web application developed for the Automated Software Testing project.

The application allows a student to organize study categories and study tasks with priorities, deadlines, and completion status. The main focus of the project is not only the web application itself, but also the testing strategy, including unit tests, web-layer tests, integration tests, Testcontainers, and code coverage.

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
- Testcontainers
- JaCoCo
- GitHub Actions

## Architecture

The project follows a layered architecture:

```text
Controller layer
Service layer
Repository layer
Entity/domain layer
PostgreSQL database