package it.unifi.ast.studyplanner.dto;

import java.time.LocalDate;

import it.unifi.ast.studyplanner.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StudyTaskForm {

	@NotBlank(message = "Task title is required")
	@Size(max = 150, message = "Task title must not exceed 150 characters")
	private String title;

	@Size(max = 1000, message = "Description must not exceed 1000 characters")
	private String description;

	@NotNull(message = "Priority is required")
	private Priority priority;

	private LocalDate dueDate;

	@NotNull(message = "Category is required")
	private Long categoryId;

	public StudyTaskForm() {
	}

	public StudyTaskForm(String title, String description, Priority priority, LocalDate dueDate, Long categoryId) {
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.dueDate = dueDate;
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}