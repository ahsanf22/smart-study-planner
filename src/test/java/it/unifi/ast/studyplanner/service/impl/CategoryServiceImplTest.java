package it.unifi.ast.studyplanner.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.unifi.ast.studyplanner.entity.Category;
import it.unifi.ast.studyplanner.exception.CategoryInUseException;
import it.unifi.ast.studyplanner.exception.DuplicateCategoryNameException;
import it.unifi.ast.studyplanner.exception.ResourceNotFoundException;
import it.unifi.ast.studyplanner.repository.CategoryRepository;
import it.unifi.ast.studyplanner.repository.StudyTaskRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private StudyTaskRepository studyTaskRepository;

	@InjectMocks
	private CategoryServiceImpl categoryService;

	@Test
	void findAllReturnsCategories() {
		Category category = new Category("Testing", "Testing tasks");
		when(categoryRepository.findAll()).thenReturn(List.of(category));

		List<Category> categories = categoryService.findAll();

		assertThat(categories).containsExactly(category);
	}

	@Test
	void findByIdReturnsCategoryWhenItExists() {
		Category category = new Category("AST", "Course category");
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

		Category found = categoryService.findById(1L);

		assertThat(found).isSameAs(category);
	}

	@Test
	void findByIdThrowsWhenCategoryDoesNotExist() {
		when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> categoryService.findById(99L))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage("Category not found with id: 99");
	}

	@Test
	void createCategorySavesCategoryWhenNameIsUnique() {
		when(categoryRepository.findByName("Programming")).thenReturn(Optional.empty());

		categoryService.createCategory(" Programming ", "Programming tasks");

		ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
		verify(categoryRepository).save(captor.capture());

		assertThat(captor.getValue().getName()).isEqualTo("Programming");
		assertThat(captor.getValue().getDescription()).isEqualTo("Programming tasks");
	}

	@Test
	void createCategoryRejectsDuplicateName() {
		Category existing = new Category("Testing", "Existing category");
		when(categoryRepository.findByName("Testing")).thenReturn(Optional.of(existing));

		assertThatThrownBy(() -> categoryService.createCategory("Testing", "Duplicate"))
				.isInstanceOf(DuplicateCategoryNameException.class)
				.hasMessage("Category already exists with name: Testing");
	}

	@Test
	void createCategoryRejectsBlankName() {
		assertThatThrownBy(() -> categoryService.createCategory("   ", "Invalid"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Category name must not be empty");
	}

	@Test
	void createCategoryRejectsNullName() {
		assertThatThrownBy(() -> categoryService.createCategory(null, "Invalid"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Category name must not be empty");
	}

	@Test
	void updateCategorySavesUpdatedCategoryWhenNameIsAvailable() {
		Category category = new Category("Old", "Old description");
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(categoryRepository.findByName("New")).thenReturn(Optional.empty());
		when(categoryRepository.save(category)).thenReturn(category);

		Category updated = categoryService.updateCategory(1L, " New ", "New description");

		assertThat(updated).isSameAs(category);
		assertThat(category.getName()).isEqualTo("New");
		assertThat(category.getDescription()).isEqualTo("New description");
		verify(categoryRepository).save(category);
	}

	@Test
	void updateCategoryAllowsKeepingTheSameName() {
		Category category = new Category("AST", "Old description");
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(categoryRepository.findByName("AST")).thenReturn(Optional.of(category));
		when(categoryRepository.save(category)).thenReturn(category);

		Category updated = categoryService.updateCategory(1L, "AST", "Updated description");

		assertThat(updated).isSameAs(category);
		assertThat(category.getDescription()).isEqualTo("Updated description");
		verify(categoryRepository).save(category);
	}

	@Test
	void updateCategoryRejectsNameUsedByAnotherCategory() {
		Category category = new Category("AST", "Old description");
		Category otherCategory = new Category("Math", "Other category");
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(categoryRepository.findByName("Math")).thenReturn(Optional.of(otherCategory));

		assertThatThrownBy(() -> categoryService.updateCategory(1L, "Math", "Updated description"))
				.isInstanceOf(DuplicateCategoryNameException.class)
				.hasMessage("Category already exists with name: Math");

		verify(categoryRepository, never()).save(category);
	}

	@Test
	void updateCategoryRejectsMissingCategory() {
		when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> categoryService.updateCategory(99L, "AST", "Description"))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage("Category not found with id: 99");
	}

	@Test
	void updateCategoryRejectsBlankName() {
		Category category = new Category("AST", "Old description");
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

		assertThatThrownBy(() -> categoryService.updateCategory(1L, " ", "Description"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Category name must not be empty");

		verify(categoryRepository, never()).save(category);
	}

	@Test
	void deleteCategoryDeletesExistingUnusedCategory() {
		when(categoryRepository.existsById(1L)).thenReturn(true);
		when(studyTaskRepository.existsByCategoryId(1L)).thenReturn(false);

		categoryService.deleteCategory(1L);

		verify(categoryRepository).deleteById(1L);
	}

	@Test
	void deleteCategoryRejectsMissingCategory() {
		when(categoryRepository.existsById(99L)).thenReturn(false);

		assertThatThrownBy(() -> categoryService.deleteCategory(99L))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage("Category not found with id: 99");

		verify(categoryRepository, never()).deleteById(99L);
	}

	@Test
	void deleteCategoryRejectsCategoryUsedByTasks() {
		when(categoryRepository.existsById(1L)).thenReturn(true);
		when(studyTaskRepository.existsByCategoryId(1L)).thenReturn(true);

		assertThatThrownBy(() -> categoryService.deleteCategory(1L))
				.isInstanceOf(CategoryInUseException.class)
				.hasMessage("Cannot delete category because it is used by existing tasks.");

		verify(categoryRepository, never()).deleteById(1L);
	}
}
