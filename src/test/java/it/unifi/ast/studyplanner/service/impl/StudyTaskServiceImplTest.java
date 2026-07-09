package it.unifi.ast.studyplanner.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.unifi.ast.studyplanner.entity.Category;
import it.unifi.ast.studyplanner.entity.Priority;
import it.unifi.ast.studyplanner.entity.StudyTask;
import it.unifi.ast.studyplanner.entity.TaskStatus;
import it.unifi.ast.studyplanner.exception.ResourceNotFoundException;
import it.unifi.ast.studyplanner.repository.CategoryRepository;
import it.unifi.ast.studyplanner.repository.StudyTaskRepository;

@ExtendWith(MockitoExtension.class)
class StudyTaskServiceImplTest {

	@Mock
	private StudyTaskRepository studyTaskRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private StudyTaskServiceImpl studyTaskService;

	@Test
	void findAllReturnsTasks() {
		StudyTask task = task("Read chapter", TaskStatus.PENDING);
		when(studyTaskRepository.findAll()).thenReturn(List.of(task));

		List<StudyTask> tasks = studyTaskService.findAll();

		assertThat(tasks).containsExactly(task);
	}

	@Test
	void findByIdReturnsTaskWhenItExists() {
		StudyTask task = task("Read chapter", TaskStatus.PENDING);
		when(studyTaskRepository.findById(1L)).thenReturn(Optional.of(task));

		StudyTask found = studyTaskService.findById(1L);

		assertThat(found).isSameAs(task);
	}

	@Test
	void findByIdThrowsWhenTaskDoesNotExist() {
		when(studyTaskRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> studyTaskService.findById(99L))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage("Task not found with id: 99");
	}

	@Test
	void createTaskSavesTaskWhenCategoryExists() {
		Category category = new Category("AST", "Automated Software Testing");
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

		studyTaskService.createTask(
				" Write service tests ",
				"Use Mockito for business logic tests",
				Priority.HIGH,
				LocalDate.now().plusDays(1),
				1L);

		ArgumentCaptor<StudyTask> captor = ArgumentCaptor.forClass(StudyTask.class);
		verify(studyTaskRepository).save(captor.capture());

		StudyTask savedTask = captor.getValue();

		assertThat(savedTask.getTitle()).isEqualTo("Write service tests");
		assertThat(savedTask.getDescription()).isEqualTo("Use Mockito for business logic tests");
		assertThat(savedTask.getPriority()).isEqualTo(Priority.HIGH);
		assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.PENDING);
		assertThat(savedTask.getCategory()).isEqualTo(category);
	}

	@Test
	void createTaskRejectsMissingCategory() {
		when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> studyTaskService.createTask(
				"Task",
				"Description",
				Priority.MEDIUM,
				LocalDate.now(),
				99L))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage("Category not found with id: 99");
	}

	@Test
	void createTaskRejectsBlankTitle() {
		assertThatThrownBy(() -> studyTaskService.createTask(
				"   ",
				"Description",
				Priority.MEDIUM,
				LocalDate.now(),
				1L))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Task title must not be empty");
	}

	@Test
	void createTaskRejectsNullTitle() {
		assertThatThrownBy(() -> studyTaskService.createTask(
				null,
				"Description",
				Priority.MEDIUM,
				LocalDate.now(),
				1L))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Task title must not be empty");
	}

	@Test
	void updateTaskSavesUpdatedTaskWhenTaskAndCategoryExist() {
		Category oldCategory = new Category("Old", "Old category");
		Category newCategory = new Category("New", "New category");
		StudyTask task = new StudyTask("Old title", "Old description", Priority.LOW, LocalDate.now(), oldCategory);
		LocalDate dueDate = LocalDate.now().plusDays(3);
		when(studyTaskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
		when(studyTaskRepository.save(task)).thenReturn(task);

		StudyTask updated = studyTaskService.updateTask(
				1L,
				" Updated title ",
				"Updated description",
				Priority.HIGH,
				dueDate,
				2L);

		assertThat(updated).isSameAs(task);
		assertThat(task.getTitle()).isEqualTo("Updated title");
		assertThat(task.getDescription()).isEqualTo("Updated description");
		assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
		assertThat(task.getDueDate()).isEqualTo(dueDate);
		assertThat(task.getCategory()).isSameAs(newCategory);
		verify(studyTaskRepository).save(task);
	}

	@Test
	void updateTaskRejectsMissingTask() {
		when(studyTaskRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> studyTaskService.updateTask(
				99L,
				"Title",
				"Description",
				Priority.MEDIUM,
				LocalDate.now(),
				1L))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage("Task not found with id: 99");
	}

	@Test
	void updateTaskRejectsMissingCategory() {
		StudyTask task = task("Existing task", TaskStatus.PENDING);
		when(studyTaskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> studyTaskService.updateTask(
				1L,
				"Title",
				"Description",
				Priority.MEDIUM,
				LocalDate.now(),
				99L))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage("Category not found with id: 99");

		verify(studyTaskRepository, never()).save(task);
	}

	@Test
	void updateTaskRejectsBlankTitle() {
		StudyTask task = task("Existing task", TaskStatus.PENDING);
		Category category = new Category("AST", "Course category");
		when(studyTaskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

		assertThatThrownBy(() -> studyTaskService.updateTask(
				1L,
				" ",
				"Description",
				Priority.MEDIUM,
				LocalDate.now(),
				2L))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Task title must not be empty");

		verify(studyTaskRepository, never()).save(task);
	}

	@Test
	void markCompletedChangesTaskStatus() {
		StudyTask task = task("Study Mockito", TaskStatus.PENDING);
		when(studyTaskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(studyTaskRepository.save(task)).thenReturn(task);

		StudyTask updated = studyTaskService.markCompleted(1L);

		assertThat(updated).isSameAs(task);
		assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
		verify(studyTaskRepository).save(task);
	}

	@Test
	void markPendingChangesTaskStatus() {
		StudyTask task = task("Study Mockito", TaskStatus.COMPLETED);
		when(studyTaskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(studyTaskRepository.save(task)).thenReturn(task);

		StudyTask updated = studyTaskService.markPending(1L);

		assertThat(updated).isSameAs(task);
		assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
		verify(studyTaskRepository).save(task);
	}

	@Test
	void findByStatusDelegatesToRepository() {
		StudyTask task = task("Completed task", TaskStatus.COMPLETED);
		when(studyTaskRepository.findByStatus(TaskStatus.COMPLETED)).thenReturn(List.of(task));

		List<StudyTask> tasks = studyTaskService.findByStatus(TaskStatus.COMPLETED);

		assertThat(tasks).containsExactly(task);
	}

	@Test
	void findByCategoryDelegatesToRepository() {
		StudyTask task = task("Category task", TaskStatus.PENDING);
		when(studyTaskRepository.findByCategoryId(1L)).thenReturn(List.of(task));

		List<StudyTask> tasks = studyTaskService.findByCategory(1L);

		assertThat(tasks).containsExactly(task);
	}

	@Test
	void searchByNullTitleReturnsAllTasks() {
		StudyTask task = task("Prepare report", TaskStatus.PENDING);
		when(studyTaskRepository.findAll()).thenReturn(List.of(task));

		List<StudyTask> results = studyTaskService.searchByTitle(null);

		assertThat(results).containsExactly(task);
	}

	@Test
	void searchByBlankTitleReturnsAllTasks() {
		StudyTask task = task("Prepare report", TaskStatus.PENDING);
		when(studyTaskRepository.findAll()).thenReturn(List.of(task));

		List<StudyTask> results = studyTaskService.searchByTitle(" ");

		assertThat(results).containsExactly(task);
	}

	@Test
	void searchByTitleTrimsSearchTermAndDelegatesToRepository() {
		StudyTask task = task("Prepare report", TaskStatus.PENDING);
		when(studyTaskRepository.findByTitleContainingIgnoreCase("report")).thenReturn(List.of(task));

		List<StudyTask> results = studyTaskService.searchByTitle(" report ");

		assertThat(results).containsExactly(task);
	}

	@Test
	void deleteTaskDeletesExistingTask() {
		when(studyTaskRepository.existsById(1L)).thenReturn(true);

		studyTaskService.deleteTask(1L);

		verify(studyTaskRepository).deleteById(1L);
	}

	@Test
	void deleteTaskRejectsMissingTask() {
		when(studyTaskRepository.existsById(99L)).thenReturn(false);

		assertThatThrownBy(() -> studyTaskService.deleteTask(99L))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage("Task not found with id: 99");

		verify(studyTaskRepository, never()).deleteById(99L);
	}

	private StudyTask task(String title, TaskStatus status) {
		Category category = new Category("AST", "Automated Software Testing");
		StudyTask task = new StudyTask(title, "Description", Priority.MEDIUM, LocalDate.now(), category);
		if (status == TaskStatus.COMPLETED) {
			task.markCompleted();
		}
		return task;
	}
}
