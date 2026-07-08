package it.unifi.ast.studyplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.unifi.ast.studyplanner.dto.CategoryForm;
import it.unifi.ast.studyplanner.entity.Category;
import it.unifi.ast.studyplanner.service.CategoryService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public String listCategories(Model model) {
		model.addAttribute("categories", categoryService.findAll());
		return "categories/list";
	}

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("categoryForm", new CategoryForm());
		return "categories/form";
	}

	@PostMapping
	public String createCategory(@Valid CategoryForm categoryForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "categories/form";
		}

		categoryService.createCategory(categoryForm.getName(), categoryForm.getDescription());
		return "redirect:/categories";
	}

	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable Long id, Model model) {
		Category category = categoryService.findById(id);

		model.addAttribute("categoryId", id);
		model.addAttribute("categoryForm", new CategoryForm(category.getName(), category.getDescription()));

		return "categories/form";
	}

	@PostMapping("/{id}")
	public String updateCategory(@PathVariable Long id, @Valid CategoryForm categoryForm,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("categoryId", id);
			return "categories/form";
		}

		categoryService.updateCategory(id, categoryForm.getName(), categoryForm.getDescription());
		return "redirect:/categories";
	}

	@PostMapping("/{id}/delete")
	public String deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return "redirect:/categories";
	}
}