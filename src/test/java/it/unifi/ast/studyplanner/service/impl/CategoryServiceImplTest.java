package it.unifi.ast.studyplanner.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import it.unifi.ast.studyplanner.exception.CategoryInUseException;
import it.unifi.ast.studyplanner.repository.StudyTaskRepository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.unifi.ast.studyplanner.entity.Category;
import it.unifi.ast.studyplanner.exception.DuplicateCategoryNameException;
import it.unifi.ast.studyplanner.exception.ResourceNotFoundException;
import it.unifi.ast.studyplanner.repository.CategoryRepository;

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
				.isInstanceOf(DuplicateCategoryNameException.class);
	}

	@Test
	void createCategoryRejectsBlankName() {
		assertThatThrownBy(() -> categoryService.createCategory("   ", "Invalid"))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void findByIdThrowsWhenCategoryDoesNotExist() {
		when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> categoryService.findById(99L))
				.isInstanceOf(ResourceNotFoundException.class);
	}
	
	@Test
	void deleteCategoryRejectsCategoryUsedByTasks() {
		when(categoryRepository.existsById(1L)).thenReturn(true);
		when(studyTaskRepository.existsByCategoryId(1L)).thenReturn(true);

		assertThatThrownBy(() -> categoryService.deleteCategory(1L))
				.isInstanceOf(CategoryInUseException.class);

		verify(categoryRepository, never()).deleteById(1L);
	}
}