package it.unifi.ast.studyplanner.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unifi.ast.studyplanner.entity.StudyTask;
import it.unifi.ast.studyplanner.entity.TaskStatus;

public interface StudyTaskRepository extends JpaRepository<StudyTask, Long> {

	List<StudyTask> findByStatus(TaskStatus status);

	List<StudyTask> findByCategoryId(Long categoryId);

	List<StudyTask> findByTitleContainingIgnoreCase(String title);

	List<StudyTask> findByDueDateBeforeAndStatus(LocalDate dueDate, TaskStatus status);
}