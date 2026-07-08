package it.unifi.ast.studyplanner.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unifi.ast.studyplanner.entity.Category;
import it.unifi.ast.studyplanner.entity.Priority;
import it.unifi.ast.studyplanner.entity.StudyTask;
import it.unifi.ast.studyplanner.entity.TaskStatus;
import it.unifi.ast.studyplanner.exception.ResourceNotFoundException;
import it.unifi.ast.studyplanner.repository.CategoryRepository;
import it.unifi.ast.studyplanner.repository.StudyTaskRepository;
import it.unifi.ast.studyplanner.service.StudyTaskService;

@Service
@Transactional
public class StudyTaskServiceImpl implements StudyTaskService {

	private final StudyTaskRepository studyTaskRepository;
	private final CategoryRepository categoryRepository;

	public StudyTaskServiceImpl(StudyTaskRepository studyTaskRepository, CategoryRepository categoryRepository) {
		this.studyTaskRepository = studyTaskRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<StudyTask> findAll() {
		return studyTaskRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public StudyTask findById(Long id) {
		return studyTaskRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
	}

	@Override
	public StudyTask createTask(String title, String description, Priority priority, LocalDate dueDate, Long categoryId) {
		String normalizedTitle = normalizeTitle(title);
		Category category = findCategory(categoryId);

		StudyTask task = new StudyTask(normalizedTitle, description, priority, dueDate, category);
		return studyTaskRepository.save(task);
	}

	@Override
	public StudyTask updateTask(Long id, String title, String description, Priority priority, LocalDate dueDate,
			Long categoryId) {
		StudyTask task = findById(id);
		Category category = findCategory(categoryId);

		task.setTitle(normalizeTitle(title));
		task.setDescription(description);
		task.setPriority(priority);
		task.setDueDate(dueDate);
		task.setCategory(category);

		return studyTaskRepository.save(task);
	}

	@Override
	public StudyTask markCompleted(Long id) {
		StudyTask task = findById(id);
		task.markCompleted();
		return studyTaskRepository.save(task);
	}

	@Override
	public StudyTask markPending(Long id) {
		StudyTask task = findById(id);
		task.markPending();
		return studyTaskRepository.save(task);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StudyTask> findByStatus(TaskStatus status) {
		return studyTaskRepository.findByStatus(status);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StudyTask> findByCategory(Long categoryId) {
		return studyTaskRepository.findByCategoryId(categoryId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StudyTask> searchByTitle(String title) {
		if (title == null || title.trim().isEmpty()) {
			return findAll();
		}

		return studyTaskRepository.findByTitleContainingIgnoreCase(title.trim());
	}

	@Override
	public void deleteTask(Long id) {
		if (!studyTaskRepository.existsById(id)) {
			throw new ResourceNotFoundException("Task not found with id: " + id);
		}

		studyTaskRepository.deleteById(id);
	}

	private Category findCategory(Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
	}

	private String normalizeTitle(String title) {
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Task title must not be empty");
		}

		return title.trim();
	}
}