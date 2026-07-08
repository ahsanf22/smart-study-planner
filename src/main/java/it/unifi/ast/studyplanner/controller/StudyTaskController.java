package it.unifi.ast.studyplanner.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.unifi.ast.studyplanner.dto.StudyTaskForm;
import it.unifi.ast.studyplanner.entity.Priority;
import it.unifi.ast.studyplanner.entity.StudyTask;
import it.unifi.ast.studyplanner.entity.TaskStatus;
import it.unifi.ast.studyplanner.service.CategoryService;
import it.unifi.ast.studyplanner.service.StudyTaskService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/tasks")
public class StudyTaskController {

	private final StudyTaskService studyTaskService;
	private final CategoryService categoryService;

	public StudyTaskController(StudyTaskService studyTaskService, CategoryService categoryService) {
		this.studyTaskService = studyTaskService;
		this.categoryService = categoryService;
	}

	@GetMapping
	public String listTasks(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) TaskStatus status,
			@RequestParam(required = false) Long categoryId,
			Model model) {

		List<StudyTask> tasks;

		if (search != null && !search.trim().isEmpty()) {
			tasks = studyTaskService.searchByTitle(search);
		} else if (status != null) {
			tasks = studyTaskService.findByStatus(status);
		} else if (categoryId != null) {
			tasks = studyTaskService.findByCategory(categoryId);
		} else {
			tasks = studyTaskService.findAll();
		}

		model.addAttribute("tasks", tasks);
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("statuses", TaskStatus.values());
		model.addAttribute("selectedStatus", status);
		model.addAttribute("selectedCategoryId", categoryId);
		model.addAttribute("search", search);

		return "tasks/list";
	}

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("taskForm", new StudyTaskForm());
		addReferenceData(model);
		return "tasks/form";
	}

	@PostMapping
	public String createTask(@Valid @ModelAttribute("taskForm") StudyTaskForm taskForm,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			addReferenceData(model);
			return "tasks/form";
		}

		studyTaskService.createTask(
				taskForm.getTitle(),
				taskForm.getDescription(),
				taskForm.getPriority(),
				taskForm.getDueDate(),
				taskForm.getCategoryId());

		return "redirect:/tasks";
	}

	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable Long id, Model model) {
		StudyTask task = studyTaskService.findById(id);

		model.addAttribute("taskId", id);
		model.addAttribute("taskForm", new StudyTaskForm(
				task.getTitle(),
				task.getDescription(),
				task.getPriority(),
				task.getDueDate(),
				task.getCategory().getId()));

		addReferenceData(model);

		return "tasks/form";
	}

	@PostMapping("/{id}")
	public String updateTask(@PathVariable Long id,
			@Valid @ModelAttribute("taskForm") StudyTaskForm taskForm,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("taskId", id);
			addReferenceData(model);
			return "tasks/form";
		}

		studyTaskService.updateTask(
				id,
				taskForm.getTitle(),
				taskForm.getDescription(),
				taskForm.getPriority(),
				taskForm.getDueDate(),
				taskForm.getCategoryId());

		return "redirect:/tasks";
	}

	@PostMapping("/{id}/complete")
	public String markCompleted(@PathVariable Long id) {
		studyTaskService.markCompleted(id);
		return "redirect:/tasks";
	}

	@PostMapping("/{id}/pending")
	public String markPending(@PathVariable Long id) {
		studyTaskService.markPending(id);
		return "redirect:/tasks";
	}

	@PostMapping("/{id}/delete")
	public String deleteTask(@PathVariable Long id) {
		studyTaskService.deleteTask(id);
		return "redirect:/tasks";
	}

	private void addReferenceData(Model model) {
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("priorities", Priority.values());
	}
}