package it.unifi.ast.studyplanner.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import it.unifi.ast.studyplanner.entity.Category;
import it.unifi.ast.studyplanner.support.AbstractPostgresContainerTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest extends AbstractPostgresContainerTest {

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void savesAndFindsCategoryByName() {
		Category category = new Category("Automated Software Testing", "Tasks related to the AST course");

		categoryRepository.save(category);

		Optional<Category> found = categoryRepository.findByName("Automated Software Testing");

		assertThat(found).isPresent();
		assertThat(found.get().getDescription()).isEqualTo("Tasks related to the AST course");
		assertThat(found.get().getCreatedAt()).isNotNull();
		assertThat(found.get().getUpdatedAt()).isNotNull();
	}
}