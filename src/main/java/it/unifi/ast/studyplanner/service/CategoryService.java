package it.unifi.ast.studyplanner.service;

import java.util.List;

import it.unifi.ast.studyplanner.entity.Category;

public interface CategoryService {

	List<Category> findAll();

	Category findById(Long id);

	Category createCategory(String name, String description);

	Category updateCategory(Long id, String name, String description);

	void deleteCategory(Long id);
}