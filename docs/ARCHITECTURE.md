# Architecture

Smart Study Planner follows a layered Spring Boot architecture. The design separates presentation logic, business logic, persistence logic, and domain modeling so that each part of the application can be tested independently.

## Architectural Style

The application follows a classic layered architecture:

```text
Controller layer
DTO/Form layer
Service layer
Repository layer
Entity/domain layer
PostgreSQL database
```

## Controller Layer

The controller layer handles HTTP requests, form submissions, redirects, validation results, and view rendering.

Main controllers:

- `HomeController`
- `CategoryController`
- `StudyTaskController`

Responsibilities:

- Receive browser requests
- Prepare model data for Thymeleaf templates
- Validate form input
- Redirect after successful form submissions
- Display user-friendly error messages

## DTO/Form Layer

The DTO/Form layer contains objects used for form binding and validation.

Main form classes:

- `CategoryForm`
- `StudyTaskForm`

Responsibilities:

- Store input from Thymeleaf forms
- Apply validation annotations
- Separate web input models from JPA entities

## Service Layer

The service layer contains the main business logic of the application.

Main services:

- `CategoryService`
- `StudyTaskService`
- `CategoryServiceImpl`
- `StudyTaskServiceImpl`

Business rules include:

- Category names must not be blank
- Duplicate category names are rejected
- A task must belong to an existing category
- A category cannot be deleted if it is used by existing tasks
- A task can be marked as completed
- A completed task can be reopened as pending

## Repository Layer

The repository layer uses Spring Data JPA to access the PostgreSQL database.

Main repositories:

- `CategoryRepository`
- `StudyTaskRepository`

Responsibilities:

- Store and retrieve entities
- Query categories by name
- Query tasks by status, category, title, and deadline
- Use entity graphs where needed to avoid lazy loading problems in views

## Entity Layer

The entity/domain layer represents the core data model.

Main domain classes:

- `Category`
- `StudyTask`
- `Priority`
- `TaskStatus`

## Domain Model

A `Category` represents a course, subject, or study area.

A `StudyTask` represents a study activity with title, description, priority, due date, status, and category.

Relationship:

```text
Category 1 ---- * StudyTask
```

Each task belongs to one category. A category can contain many tasks.

## Database

PostgreSQL is used as the development database.

The development database is usually started with Docker:

```bash
docker start studyplanner-db
```

Repository integration tests use PostgreSQL Testcontainers. This allows tests to run against a real PostgreSQL database in a reproducible way.

## Error Handling

The application includes user-friendly error handling for common cases:

- Duplicate category names
- Attempting to delete a category that is still used by tasks
- Invalid form input

Instead of showing technical error pages, the application displays validation or warning messages in the web interface.

## Testability

The architecture supports automated testing because each layer has clear responsibilities:

- Service logic can be tested with Mockito
- Controllers can be tested with MockMvc
- Repositories can be tested with Testcontainers
- Full application configuration can be checked with a Spring context test
EOF