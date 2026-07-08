package it.unifi.ast.studyplanner.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import it.unifi.ast.studyplanner.entity.Category;
import it.unifi.ast.studyplanner.entity.Priority;
import it.unifi.ast.studyplanner.entity.StudyTask;
import it.unifi.ast.studyplanner.service.CategoryService;
import it.unifi.ast.studyplanner.service.StudyTaskService;

@WebMvcTest(StudyTaskController.class)
class StudyTaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private StudyTaskService studyTaskService;

	@MockitoBean
	private CategoryService categoryService;

	@Test
	void listTasksReturnsTaskListView() throws Exception {
		Category category = new Category("AST", "Testing course");
		StudyTask task = new StudyTask("Write tests", "Controller tests", Priority.HIGH, LocalDate.now(), category);

		when(studyTaskService.findAll()).thenReturn(List.of(task));
		when(categoryService.findAll()).thenReturn(List.of(category));

		mockMvc.perform(get("/tasks"))
				.andExpect(status().isOk())
				.andExpect(view().name("tasks/list"))
				.andExpect(model().attributeExists("tasks"))
				.andExpect(model().attributeExists("categories"));
	}

	@Test
	void showCreateFormReturnsTaskFormView() throws Exception {
		when(categoryService.findAll()).thenReturn(List.of(new Category("AST", "Testing course")));

		mockMvc.perform(get("/tasks/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("tasks/form"))
				.andExpect(model().attributeExists("taskForm"))
				.andExpect(model().attributeExists("categories"))
				.andExpect(model().attributeExists("priorities"));
	}

	@Test
	void createTaskRedirectsToTaskList() throws Exception {
		mockMvc.perform(post("/tasks")
				.param("title", "Write service tests")
				.param("description", "Use Mockito")
				.param("priority", "HIGH")
				.param("dueDate", "2026-07-10")
				.param("categoryId", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/tasks"));

		verify(studyTaskService).createTask(
				"Write service tests",
				"Use Mockito",
				Priority.HIGH,
				LocalDate.of(2026, 7, 10),
				1L);
	}

	@Test
	void createTaskWithBlankTitleReturnsForm() throws Exception {
		when(categoryService.findAll()).thenReturn(List.of(new Category("AST", "Testing course")));

		mockMvc.perform(post("/tasks")
				.param("title", "")
				.param("description", "Invalid")
				.param("priority", "HIGH")
				.param("dueDate", "2026-07-10")
				.param("categoryId", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("tasks/form"));
	}

	@Test
	void markCompletedRedirectsToTaskList() throws Exception {
		mockMvc.perform(post("/tasks/1/complete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/tasks"));

		verify(studyTaskService).markCompleted(1L);
	}
}