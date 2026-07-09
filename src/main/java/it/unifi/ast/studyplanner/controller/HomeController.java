package it.unifi.ast.studyplanner.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.unifi.ast.studyplanner.entity.StudyTask;
import it.unifi.ast.studyplanner.entity.TaskStatus;
import it.unifi.ast.studyplanner.service.CategoryService;
import it.unifi.ast.studyplanner.service.StudyTaskService;

@Controller
class HomeController {

	private final CategoryService categoryService;
	private final StudyTaskService studyTaskService;

	HomeController(CategoryService categoryService, StudyTaskService studyTaskService) {
		this.categoryService = categoryService;
		this.studyTaskService = studyTaskService;
	}

	@GetMapping("/")
	String home(Model model) {
		List<StudyTask> tasks = studyTaskService.findAll();

		long pendingTasks = tasks.stream()
				.filter(task -> TaskStatus.PENDING.equals(task.getStatus()))
				.count();

		long completedTasks = tasks.stream()
				.filter(task -> TaskStatus.COMPLETED.equals(task.getStatus()))
				.count();

		model.addAttribute("totalCategories", categoryService.findAll().size());
		model.addAttribute("totalTasks", tasks.size());
		model.addAttribute("pendingTasks", pendingTasks);
		model.addAttribute("completedTasks", completedTasks);
		model.addAttribute("recentTasks", tasks.stream().limit(5).toList());

		return "index";
	}
}