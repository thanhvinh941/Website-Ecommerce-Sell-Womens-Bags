package com.ecommerce.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.category.CategoryPageInfo;
import com.ecommerce.admin.exception.CategoryNotFoudException;
import com.ecommerce.admin.export.CategoryCSVExport;
import com.ecommerce.admin.service.CategoryService;
import com.ecommerce.admin.service.UserService;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.User;

@Controller
public class CategoryController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public String listFirstPage(
			HttpServletRequest request,
			@Param("sortDir") String sortDir, 
			Model model) {
		return listByPage(request, 1, sortDir, null, "id", model);
	}

	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(HttpServletRequest request,
			@PathVariable(name = "pageNum") int pageNum, 
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword, 
			@Param("sortField") String sortField, 
			Model model) {
		String email = userService.getEmailOfAuthenticatedUser(request);
		User user = userService.getUserByEmail(email);
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		CategoryPageInfo categoryPageInfo = new CategoryPageInfo();

		List<Category> listCategories = categoryService.listByPage(categoryPageInfo, pageNum, sortDir, sortField,
				keyword);
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > categoryPageInfo.getTotalElements()) {
			endCount = categoryPageInfo.getTotalElements();
		}
		
		model.addAttribute("user", user);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPage", categoryPageInfo.getTotalPages());
		model.addAttribute("totalItem", categoryPageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		
		return "categories/categories";
	}

	@GetMapping("/categories/new")
	public String newCategory(
			Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Created New Category");

		return "categories/category_form";
	}

	@RequestMapping(value = "/categories/save", method = RequestMethod.POST)
	public String saveCategory(
			Category category, 
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()).replace(" ", "");
			category.setImage(fileName);

			Category saveCategory = categoryService.save(category);
			String uploadDir = "../categories-images/" + saveCategory.getId();

			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			categoryService.save(category);
		}
		redirectAttributes.addFlashAttribute("message", "The category have been save successfully.");
		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategory(
			@PathVariable(name = "id") Integer id, 
			RedirectAttributes redirectAttributes,
			Model model) {

		try {
			Category category = categoryService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			model.addAttribute("category", category);
			model.addAttribute("pageTitle", " Edit User (ID: " + id + ")");
			model.addAttribute("listCategories", listCategories);
			return "categories/category_form";
		} catch (CategoryNotFoudException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/categories";
		}
	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(
			@PathVariable("id") Integer id, 
			@PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		categoryService.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category Id: " + id + " has  been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/categories";
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(
			@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			categoryService.delete(id);
			String categoryDir = "../categories-images/" + id;
			FileUploadUtil.removeDir(categoryDir);
			redirectAttributes.addFlashAttribute("message", "The category ID " + id + " has been deleted successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/categories";
	}

	@GetMapping("/categories/export/csv")
	public void exportToCSV(
			HttpServletResponse response) throws IOException {
		List<Category> list = categoryService.listCategoriesUsedInForm();
		CategoryCSVExport export = new CategoryCSVExport();
		export.export(list, response);
	}
}
