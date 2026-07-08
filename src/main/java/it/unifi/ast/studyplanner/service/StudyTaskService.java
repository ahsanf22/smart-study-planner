package it.unifi.ast.studyplanner.service;

import java.time.LocalDate;
import java.util.List;

import it.unifi.ast.studyplanner.entity.Priority;
import it.unifi.ast.studyplanner.entity.StudyTask;
import it.unifi.ast.studyplanner.entity.TaskStatus;

public interface StudyTaskService {

	List<StudyTask> findAll();

	StudyTask findById(Long id);

	StudyTask createTask(String title, String description, Priority priority, LocalDate dueDate, Long categoryId);

	StudyTask updateTask(Long id, String title, String description, Priority priority, LocalDate dueDate, Long categoryId);

	StudyTask markCompleted(Long id);

	StudyTask markPending(Long id);

	List<StudyTask> findByStatus(TaskStatus status);

	List<StudyTask> findByCategory(Long categoryId);

	List<StudyTask> searchByTitle(String title);

	void deleteTask(Long id);
}