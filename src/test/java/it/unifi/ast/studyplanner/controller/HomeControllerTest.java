package it.unifi.ast.studyplanner.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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

@WebMvcTest(HomeController.class)
class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoryService categoryService;

	@MockitoBean
	private StudyTaskService studyTaskService;

	@Test
	void homePageReturnsDashboardView() throws Exception {
		Category category = new Category("AST", "Automated Software Testing");
		StudyTask task = new StudyTask(
				"Prepare tests",
				"Write controller tests",
				Priority.HIGH,
				LocalDate.now(),
				category);

		when(categoryService.findAll()).thenReturn(List.of(category));
		when(studyTaskService.findAll()).thenReturn(List.of(task));

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attributeExists("totalCategories"))
				.andExpect(model().attributeExists("totalTasks"))
				.andExpect(model().attributeExists("pendingTasks"))
				.andExpect(model().attributeExists("completedTasks"))
				.andExpect(model().attributeExists("recentTasks"));
	}
}