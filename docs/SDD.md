# Software Design Document

| Field | Value |
|---|---|
| Project | Smart Study Planner |
| Version | 1.0 |
| Course | Automated Software Testing |
| University | University of Florence |
| Author | Muhammad Ahsan Khan |
| Date | July 2026 |

---

## Revision History

| Version | Date | Author | Description |
|---|---|---|---|
| 0.1 | July 2026 | Muhammad Ahsan Khan | Initial draft |
| 1.0 | July 2026 | Muhammad Ahsan Khan | First complete version |

---

## Table of Contents

1. Introduction
2. Project Overview
3. Project Scope
4. Functional Requirements
5. Non-Functional Requirements
6. Use Cases
7. Software Architecture
8. Database Design
9. Web Endpoints
10. Testing Strategy
11. Development Process
12. Technology Stack
13. Project Structure
14. Future Enhancements
15. References

---

# 1. Introduction

## 1.1 Purpose

This Software Design Document describes the design and implementation of the Smart Study Planner web application developed for the Automated Software Testing course.

The purpose of the project is to demonstrate the practical application of automated testing techniques in a realistic Spring Boot web application.

The main focus is not only the application functionality, but also the testing strategy, build automation, continuous integration, and code quality tools.

## 1.2 Background

Students often manage study deadlines, assignments, and preparation tasks using manual notes or scattered tools. This can make it difficult to track priorities and progress.

Smart Study Planner provides a simple browser-based system for organizing study categories and study tasks.

The application is intentionally limited in scope so that the project can focus strongly on software quality and automated testing.

## 1.3 Objectives

The main objectives are:

- Develop a maintainable Spring Boot web application.
- Apply a layered software architecture.
- Implement automated tests at different levels.
- Use PostgreSQL as the database.
- Use Testcontainers for integration testing.
- Generate code coverage reports using JaCoCo.
- Configure PIT mutation testing.
- Configure GitHub Actions continuous integration.
- Provide documentation suitable for final project review.

---

# 2. Project Overview

Smart Study Planner is a server-side web application built with Spring Boot and Thymeleaf.

The application allows a student to:

- Create study categories
- Create study tasks
- Assign priorities and due dates
- Mark tasks as completed or pending
- Search and filter tasks
- View dashboard statistics

The project uses a layered architecture with controller, DTO/form, service, repository, and entity layers.

---

# 3. Project Scope

## 3.1 In Scope

Version 1.0 includes:

- Dashboard page
- Category creation
- Category update
- Category deletion
- Prevention of deleting categories used by tasks
- Task creation
- Task update
- Task deletion
- Marking tasks completed
- Reopening completed tasks
- Searching tasks by title
- Filtering tasks by category
- Filtering tasks by status
- Form validation
- User-friendly error messages
- Automated tests
- Code coverage report
- Mutation testing configuration
- GitHub Actions CI

## 3.2 Out of Scope

The following features are outside the scope of version 1.0:

- User authentication
- Multi-user accounts
- Role-based authorization
- Email notifications
- Calendar synchronization
- File attachments
- Mobile application
- AI recommendations
- Cloud deployment

The application is implemented as a single-user study planner to keep the focus on automated testing and maintainability.

---

# 4. Functional Requirements

## FR1: Dashboard

The system shall display a dashboard with:

- Total number of categories
- Total number of tasks
- Number of pending tasks
- Number of completed tasks
- Recent tasks

## FR2: Category Management

The system shall allow the user to:

- Create a category
- View all categories
- Edit a category
- Delete a category

The system shall reject duplicate category names.

The system shall prevent deletion of categories that are already used by existing tasks.

## FR3: Task Management

The system shall allow the user to:

- Create a study task
- View all study tasks
- Edit a study task
- Delete a study task
- Mark a task as completed
- Reopen a completed task as pending

## FR4: Task Search and Filtering

The system shall allow the user to:

- Search tasks by title
- Filter tasks by category
- Filter tasks by status

## FR5: Validation

The system shall validate form input.

Examples:

- Category name must not be blank
- Task title must not be blank
- Priority must be selected
- Category must be selected for a task

---

# 5. Non-Functional Requirements

## 5.1 Maintainability

The project shall use a clear layered architecture to support maintainability and future extension.

## 5.2 Testability

The application shall be designed so that service logic, controllers, and repositories can be tested independently.

## 5.3 Reliability

The system shall handle expected user errors, such as duplicate categories or invalid deletion attempts, without exposing technical error pages.

## 5.4 Reproducibility

The project shall be buildable using Maven.

Integration tests shall use Testcontainers to provide a reproducible PostgreSQL database environment.

## 5.5 Usability

The user interface shall provide simple navigation, clear forms, and readable feedback messages.

---

# 6. Use Cases

## UC1: Create Category

Actor: Student

Main flow:

1. Student opens the categories page.
2. Student clicks create category.
3. Student enters category name and description.
4. System validates the input.
5. System saves the category.
6. System redirects to the category list.

Alternative flow:

- If the category name already exists, the system displays a validation message.

## UC2: Create Study Task

Actor: Student

Main flow:

1. Student opens the tasks page.
2. Student clicks create task.
3. Student enters title, description, priority, due date, and category.
4. System validates the input.
5. System saves the task.
6. System redirects to the task list.

## UC3: Mark Task Completed

Actor: Student

Main flow:

1. Student opens the task list.
2. Student clicks complete on a pending task.
3. System changes the task status to completed.
4. System redirects to the task list.

## UC4: Delete Category Used by Tasks

Actor: Student

Main flow:

1. Student opens the category list.
2. Student attempts to delete a category that is used by tasks.
3. System prevents the deletion.
4. System displays a warning message.

---

# 7. Software Architecture

The application follows a layered architecture:

```text
Controller layer
DTO/Form layer
Service layer
Repository layer
Entity/domain layer
PostgreSQL database
```

## 7.1 Controller Layer

The controller layer handles web requests and returns Thymeleaf views.

Main controllers:

- `HomeController`
- `CategoryController`
- `StudyTaskController`

## 7.2 DTO/Form Layer

The DTO/Form layer contains form objects used for input binding and validation.

Main DTOs:

- `CategoryForm`
- `StudyTaskForm`

## 7.3 Service Layer

The service layer contains business rules.

Main services:

- `CategoryService`
- `StudyTaskService`

Main implementations:

- `CategoryServiceImpl`
- `StudyTaskServiceImpl`

## 7.4 Repository Layer

The repository layer uses Spring Data JPA for database access.

Main repositories:

- `CategoryRepository`
- `StudyTaskRepository`

## 7.5 Entity Layer

The entity layer contains the domain model.

Main classes:

- `Category`
- `StudyTask`
- `Priority`
- `TaskStatus`

---

# 8. Database Design

## 8.1 Category Entity

Attributes:

- `id`
- `name`
- `description`
- `createdAt`
- `updatedAt`

Rules:

- Category name is required.
- Category name must be unique.

## 8.2 StudyTask Entity

Attributes:

- `id`
- `title`
- `description`
- `priority`
- `status`
- `dueDate`
- `category`
- `createdAt`
- `updatedAt`

Rules:

- Task title is required.
- Priority is required.
- Status is either `PENDING` or `COMPLETED`.
- Each task belongs to one category.

## 8.3 Relationship

```text
Category 1 ---- * StudyTask
```

One category can contain many study tasks. Each study task belongs to one category.

---

# 9. Web Endpoints

## 9.1 Home

| Method | Path | Description |
|---|---|---|
| GET | `/` | Shows the dashboard |

## 9.2 Categories

| Method | Path | Description |
|---|---|---|
| GET | `/categories` | Lists categories |
| GET | `/categories/new` | Shows create category form |
| POST | `/categories` | Creates category |
| GET | `/categories/{id}/edit` | Shows edit category form |
| POST | `/categories/{id}` | Updates category |
| POST | `/categories/{id}/delete` | Deletes category if allowed |

## 9.3 Tasks

| Method | Path | Description |
|---|---|---|
| GET | `/tasks` | Lists, searches, and filters tasks |
| GET | `/tasks/new` | Shows create task form |
| POST | `/tasks` | Creates task |
| GET | `/tasks/{id}/edit` | Shows edit task form |
| POST | `/tasks/{id}` | Updates task |
| POST | `/tasks/{id}/complete` | Marks task completed |
| POST | `/tasks/{id}/pending` | Reopens task |
| POST | `/tasks/{id}/delete` | Deletes task |

---

# 10. Testing Strategy

The project includes multiple testing levels.

## 10.1 Unit Testing

Service-layer business logic is tested using JUnit 5 and Mockito.

Main test classes:

- `CategoryServiceImplTest`
- `StudyTaskServiceImplTest`

## 10.2 Web-Layer Testing

Controllers are tested using Spring MockMvc.

Main test classes:

- `HomeControllerTest`
- `CategoryControllerTest`
- `StudyTaskControllerTest`

## 10.3 Integration Testing

Repositories are tested using PostgreSQL Testcontainers.

Main test classes:

- `CategoryRepositoryTest`
- `StudyTaskRepositoryTest`

## 10.4 Application Context Test

The application context is tested to verify that the Spring Boot configuration starts correctly.

## 10.5 Code Coverage

JaCoCo is used to generate a test coverage report.

Command:

```bash
./mvnw clean verify
```

Report:

```text
target/site/jacoco/index.html
```

## 10.6 Mutation Testing

PIT mutation testing is configured to evaluate the effectiveness of the test suite.

Command:

```bash
./mvnw test-compile org.pitest:pitest-maven:mutationCoverage
```

Report:

```text
target/pit-reports/index.html
```

---

# 11. Development Process

The project is developed using Git and GitHub.

The development process includes:

- Meaningful commits
- Maven build automation
- Automated testing
- Continuous integration with GitHub Actions
- Documentation updates

The repository history shows the project evolution from documentation to domain model, persistence, service logic, web controllers, UI pages, testing tools, and quality tools.

---

# 12. Technology Stack

Main technologies:

- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Docker
- Git
- GitHub

Testing and quality tools:

- JUnit 5
- Mockito
- MockMvc
- Testcontainers
- JaCoCo
- PIT Mutation Testing
- GitHub Actions
- SonarQube Cloud workflow

---

# 13. Project Structure

Main source structure:

```text
src/main/java/it/unifi/ast/studyplanner
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
│   └── impl
└── SmartStudyPlannerApplication.java
```

Test source structure:

```text
src/test/java/it/unifi/ast/studyplanner
├── controller
├── repository
├── service.impl
└── support
```

Documentation:

```text
docs
├── API.md
├── ARCHITECTURE.md
├── SDD.md
└── TESTING.md
```

---

# 14. Future Enhancements

Possible future enhancements include:

- User authentication
- Multi-user support
- Email reminders
- Calendar integration
- Task sorting
- More advanced dashboard charts
- REST API
- Deployment to a cloud platform
- More complete end-to-end browser tests

---

# 15. References

- Spring Boot Documentation
- Spring Data JPA Documentation
- Thymeleaf Documentation
- JUnit 5 Documentation
- Mockito Documentation
- Testcontainers Documentation
- JaCoCo Documentation
- PIT Mutation Testing Documentation
- GitHub Actions Documentation
- SonarQube Cloud Documentation
EOF