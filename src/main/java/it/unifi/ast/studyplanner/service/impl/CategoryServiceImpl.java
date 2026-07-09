package it.unifi.ast.studyplanner.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unifi.ast.studyplanner.entity.Category;
import it.unifi.ast.studyplanner.exception.CategoryInUseException;
import it.unifi.ast.studyplanner.exception.DuplicateCategoryNameException;
import it.unifi.ast.studyplanner.exception.ResourceNotFoundException;
import it.unifi.ast.studyplanner.repository.CategoryRepository;
import it.unifi.ast.studyplanner.repository.StudyTaskRepository;
import it.unifi.ast.studyplanner.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final StudyTaskRepository studyTaskRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository, StudyTaskRepository studyTaskRepository) {
		this.categoryRepository = categoryRepository;
		this.studyTaskRepository = studyTaskRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Category findById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
	}

	@Override
	public Category createCategory(String name, String description) {
		String normalizedName = normalizeName(name);

		if (categoryRepository.findByName(normalizedName).isPresent()) {
			throw new DuplicateCategoryNameException("Category already exists with name: " + normalizedName);
		}

		Category category = new Category(normalizedName, description);
		return categoryRepository.save(category);
	}

	@Override
	public Category updateCategory(Long id, String name, String description) {
		Category category = findById(id);
		String normalizedName = normalizeName(name);

		Optional<Category> existingCategory = categoryRepository.findByName(normalizedName);
		if (existingCategory.isPresent() && existingCategory.get() != category) {
			throw new DuplicateCategoryNameException("Category already exists with name: " + normalizedName);
		}

		category.setName(normalizedName);
		category.setDescription(description);

		return categoryRepository.save(category);
	}

	@Override
	public void deleteCategory(Long id) {
		if (!categoryRepository.existsById(id)) {
			throw new ResourceNotFoundException("Category not found with id: " + id);
		}

		if (studyTaskRepository.existsByCategoryId(id)) {
			throw new CategoryInUseException("Cannot delete category because it is used by existing tasks.");
		}

		categoryRepository.deleteById(id);
	}

	private String normalizeName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Category name must not be empty");
		}

		return name.trim();
	}
}