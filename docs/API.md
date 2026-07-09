# Web Endpoints

Smart Study Planner uses Spring MVC controllers and Thymeleaf templates. The application does not expose a REST API. Instead, it provides browser-based web endpoints for managing categories, study tasks, and dashboard information.

## Home

| Method | Path | Description |
|---|---|---|
| GET | `/` | Shows the dashboard summary with task and category statistics |

## Categories

| Method | Path | Description |
|---|---|---|
| GET | `/categories` | Lists all categories |
| GET | `/categories/new` | Shows the create category form |
| POST | `/categories` | Creates a new category |
| GET | `/categories/{id}/edit` | Shows the edit category form |
| POST | `/categories/{id}` | Updates an existing category |
| POST | `/categories/{id}/delete` | Deletes a category if it is not used by existing tasks |

## Tasks

| Method | Path | Description |
|---|---|---|
| GET | `/tasks` | Lists all tasks and supports search/filter parameters |
| GET | `/tasks/new` | Shows the create task form |
| POST | `/tasks` | Creates a new study task |
| GET | `/tasks/{id}/edit` | Shows the edit task form |
| POST | `/tasks/{id}` | Updates an existing task |
| POST | `/tasks/{id}/complete` | Marks a task as completed |
| POST | `/tasks/{id}/pending` | Reopens a completed task |
| POST | `/tasks/{id}/delete` | Deletes a study task |

## Query Parameters for `/tasks`

| Parameter | Description |
|---|---|
| `search` | Searches tasks by title |
| `status` | Filters tasks by status: `PENDING` or `COMPLETED` |
| `categoryId` | Filters tasks by category |

## Notes

The application uses server-side rendering with Thymeleaf. Form validation is handled using Jakarta Bean Validation and controller-level error handling.
