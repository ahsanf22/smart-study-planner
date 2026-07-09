package it.unifi.ast.studyplanner.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import it.unifi.ast.studyplanner.entity.StudyTask;
import it.unifi.ast.studyplanner.entity.TaskStatus;

public interface StudyTaskRepository extends JpaRepository<StudyTask, Long> {

	@Override
	@EntityGraph(attributePaths = "category")
	List<StudyTask> findAll();

	@Override
	@EntityGraph(attributePaths = "category")
	Optional<StudyTask> findById(Long id);

	@EntityGraph(attributePaths = "category")
	List<StudyTask> findByStatus(TaskStatus status);

	@EntityGraph(attributePaths = "category")
	List<StudyTask> findByCategoryId(Long categoryId);

	boolean existsByCategoryId(Long categoryId);

	@EntityGraph(attributePaths = "category")
	List<StudyTask> findByTitleContainingIgnoreCase(String title);

	@EntityGraph(attributePaths = "category")
	List<StudyTask> findByDueDateBeforeAndStatus(LocalDate dueDate, TaskStatus status);
	
	
}