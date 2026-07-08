package it.unifi.ast.studyplanner.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
				.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void createTaskRejectsBlankTitle() {
		assertThatThrownBy(() -> studyTaskService.createTask(
				"   ",
				"Description",
				Priority.MEDIUM,
				LocalDate.now(),
				1L))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void markCompletedChangesTaskStatus() {
		Category category = new Category("Testing", "Testing tasks");
		StudyTask task = new StudyTask(
				"Study Mockito",
				"Practice mocking",
				Priority.MEDIUM,
				LocalDate.now(),
				category);

		when(studyTaskRepository.findById(1L)).thenReturn(Optional.of(task));

		studyTaskService.markCompleted(1L);

		assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
		verify(studyTaskRepository).save(task);
	}

	@Test
	void searchByBlankTitleReturnsAllTasks() {
		Category category = new Category("Documentation", "Documentation tasks");
		StudyTask task = new StudyTask(
				"Prepare report",
				"Write project report",
				Priority.HIGH,
				LocalDate.now(),
				category);

		when(studyTaskRepository.findAll()).thenReturn(List.of(task));

		List<StudyTask> results = studyTaskService.searchByTitle(" ");

		assertThat(results).containsExactly(task);
	}
}