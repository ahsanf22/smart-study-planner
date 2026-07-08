package it.unifi.ast.studyplanner.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import it.unifi.ast.studyplanner.entity.Category;
import it.unifi.ast.studyplanner.entity.Priority;
import it.unifi.ast.studyplanner.entity.StudyTask;
import it.unifi.ast.studyplanner.entity.TaskStatus;
import it.unifi.ast.studyplanner.support.AbstractPostgresContainerTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudyTaskRepositoryTest extends AbstractPostgresContainerTest {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private StudyTaskRepository studyTaskRepository;

	@Test
	void savesTaskWithCategoryAndDefaultPendingStatus() {
		Category category = categoryRepository.save(new Category("Programming", "Programming study tasks"));

		StudyTask task = new StudyTask(
				"Implement repository tests",
				"Write integration tests using Testcontainers",
				Priority.HIGH,
				LocalDate.now().plusDays(1),
				category);

		StudyTask saved = studyTaskRepository.save(task);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getStatus()).isEqualTo(TaskStatus.PENDING);
		assertThat(saved.isCompleted()).isFalse();
		assertThat(saved.getCreatedAt()).isNotNull();
		assertThat(saved.getUpdatedAt()).isNotNull();
	}

	@Test
	void findsTasksByStatus() {
		Category category = categoryRepository.save(new Category("Testing", "Testing tasks"));

		StudyTask task = new StudyTask(
				"Study JUnit",
				"Review unit testing concepts",
				Priority.MEDIUM,
				LocalDate.now().plusDays(2),
				category);

		studyTaskRepository.save(task);

		List<StudyTask> pendingTasks = studyTaskRepository.findByStatus(TaskStatus.PENDING);

		assertThat(pendingTasks)
				.extracting(StudyTask::getTitle)
				.contains("Study JUnit");
	}

	@Test
	void findsTasksByTitleIgnoringCase() {
		Category category = categoryRepository.save(new Category("Documentation", "Documentation tasks"));

		StudyTask task = new StudyTask(
				"Prepare SDD",
				"Complete the software design document",
				Priority.HIGH,
				LocalDate.now(),
				category);

		studyTaskRepository.save(task);

		List<StudyTask> results = studyTaskRepository.findByTitleContainingIgnoreCase("sdd");

		assertThat(results)
				.extracting(StudyTask::getTitle)
				.contains("Prepare SDD");
	}
}