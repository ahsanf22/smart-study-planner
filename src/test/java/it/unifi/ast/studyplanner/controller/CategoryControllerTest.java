package it.unifi.ast.studyplanner.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import it.unifi.ast.studyplanner.exception.DuplicateCategoryNameException;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import it.unifi.ast.studyplanner.entity.Category;
import it.unifi.ast.studyplanner.service.CategoryService;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoryService categoryService;

	@Test
	void listCategoriesReturnsCategoryListView() throws Exception {
		when(categoryService.findAll()).thenReturn(List.of(new Category("Testing", "Testing tasks")));

		mockMvc.perform(get("/categories"))
				.andExpect(status().isOk())
				.andExpect(view().name("categories/list"))
				.andExpect(model().attributeExists("categories"));
	}

	@Test
	void showCreateFormReturnsCategoryFormView() throws Exception {
		mockMvc.perform(get("/categories/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("categories/form"))
				.andExpect(model().attributeExists("categoryForm"));
	}

	@Test
	void createCategoryRedirectsToCategoryList() throws Exception {
		mockMvc.perform(post("/categories")
				.param("name", "Testing")
				.param("description", "Testing category"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/categories"));

		verify(categoryService).createCategory("Testing", "Testing category");
	}

	@Test
	void createCategoryWithBlankNameReturnsForm() throws Exception {
		mockMvc.perform(post("/categories")
				.param("name", "")
				.param("description", "Invalid category"))
				.andExpect(status().isOk())
				.andExpect(view().name("categories/form"));
	}
	
	@Test
	void createCategoryWithDuplicateNameReturnsFormWithError() throws Exception {
		when(categoryService.createCategory("Automated Software Testing", "Duplicate category"))
				.thenThrow(new DuplicateCategoryNameException(
						"Category already exists with name: Automated Software Testing"));

		mockMvc.perform(post("/categories")
				.param("name", "Automated Software Testing")
				.param("description", "Duplicate category"))
				.andExpect(status().isOk())
				.andExpect(view().name("categories/form"))
				.andExpect(model().attributeHasFieldErrors("categoryForm", "name"));
	}
}