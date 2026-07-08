# Software Design Document (SDD)

| **Project** | Smart Study Planner |
|--------------|---------------------|
| **Version** | 1.0 |
| **Course** | Automated Software Testing |
| **University** | University of Florence |
| **Author** | Muhammad Ahsan Khan |
| **Supervisor** | Prof. Lorenzo Bettini |
| **Date** | July 2026 |

---

# Revision History

| Version | Date | Author | Description |
|---------|------|--------|-------------|
| 0.1 | July 2026 | Muhammad Ahsan Khan | Initial draft |
| 1.0 | July 2026 | Muhammad Ahsan Khan | First complete version |

---

# Table of Contents

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


# 1. Introduction

## 1.1 Purpose

This Software Design Document (SDD) describes the design and implementation of the Smart Study Planner web application developed for the Automated Software Testing course at the University of Florence.

The document serves as the main technical reference throughout the development process. It defines the system requirements, software architecture, database structure, testing strategy, and development methodology that guide the implementation of the project.

The primary goal of this document is to ensure that every stage of development follows a clear and consistent design while maintaining software quality, maintainability, and testability.

---

## 1.2 Background

Managing study activities is often difficult when assignments, deadlines, and learning materials are spread across different platforms or maintained manually. Students may lose track of upcoming deadlines or find it difficult to prioritize their work effectively.

The Smart Study Planner addresses this problem by providing a simple web application that enables students to organize study tasks into categories, assign priorities and deadlines, monitor completion status, and view overall study progress through a dashboard.

Although the application solves a practical problem, the primary educational objective of the project is to demonstrate the application of software engineering principles and automated software testing techniques.

---

## 1.3 Objectives

The main objectives of this project are:

- Design and develop a maintainable web application using Spring Boot.
- Apply Test-Driven Development (TDD) throughout implementation.
- Demonstrate unit, integration, and web-layer testing.
- Build a clean layered architecture following separation of concerns.
- Use continuous integration and automated quality checks.
- Produce software that is easy to maintain, understand, and extend.

# 2. Project Overview

Smart Study Planner is a server-side web application developed using Spring Boot and Thymeleaf.

The application allows students to organize their academic work by creating categories and study tasks, assigning priorities, defining deadlines, and tracking completion progress. A dashboard provides an overview of pending and completed tasks to help users monitor their study activities.

Unlike commercial productivity applications, the project intentionally focuses on a limited feature set so that greater emphasis can be placed on software quality, automated testing, maintainability, and clean software architecture.

The project is designed using a layered architecture consisting of presentation, business, and persistence layers, making it suitable for testing, future enhancements, and long-term maintainability.

# 3. Project Scope

## 3.1 In Scope

Version 1.0 of the Smart Study Planner includes the following functionality:

- Create, update, and delete study tasks.
- Organize tasks into categories.
- Assign priorities to study tasks.
- Define due dates for each task.
- Mark tasks as completed.
- Search tasks by title.
- Filter tasks by category.
- Filter tasks by completion status.
- Display study statistics through a dashboard.

---

## 3.2 Out of Scope

The following features are intentionally excluded from Version 1.0:

- Multi-user support
- User authentication and authorization
- Email notifications
- Calendar synchronization
- Mobile application
- File attachments
- AI-generated study recommendations
- Cloud deployment

These features are outside the scope of the current project and may be considered in future versions.







