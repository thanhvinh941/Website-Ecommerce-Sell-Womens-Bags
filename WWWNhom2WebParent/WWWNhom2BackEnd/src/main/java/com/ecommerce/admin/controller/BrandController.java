package com.ecommerce.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.export.BrandCSVExport;
import com.ecommerce.admin.export.CategoryCSVExport;
import com.ecommerce.admin.service.BrandService;
import com.ecommerce.admin.service.CategoryService;
import com.ecommerce.admin.service.UserService;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.User;

@Controller
public class BrandController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/brands")
	public String listFirstPage(
			HttpServletRequest request,
			@Param("sortDir") String sortDir, 
			Model model) {
		return listByPage(request, 1, "id", sortDir, null, model);
	}

	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			HttpServletRequest request,
			@PathVariable(name = "pageNum") int pageNum, 
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir, 
			@Param("keyword") String keyword, 
			Model model) {

		String email = userService.getEmailOfAuthenticatedUser(request);
		User user = userService.getUserByEmail(email);
		
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Brand> listBrands = page.getContent();

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("user", user);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("totalItem", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("reverseSortDir", reverseSortDir);

		return "brands/brands";
	}

	@GetMapping("/brands/new")
	public String newBrand(
			Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		model.addAttribute("brand", new Brand());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Created New Category");

		return "brands/brand_form";
	}

	@PostMapping("/brands/save")
	public String saveBrand(
			Brand brand, 
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);

			Brand saveBrand = brandService.save(brand);
			String uploadDir = "../brands-images/" + saveBrand.getId();

			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			brandService.save(brand);
		}

		redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully.");
		return "redirect:/brands";
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrand(
			@PathVariable(name = "id") Integer id, 
			Model model, 
			RedirectAttributes redirectAttributes) {

		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();

			model.addAttribute("brand", brand);
			model.addAttribute("pageTitle", " Edit Brand (ID: " + id + ")");
			model.addAttribute("listCategories", listCategories);
			return "brands/brand_form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(
			@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {

		try {
			brandService.delete(id);
			String brandDir = "../brands-images/" + id;
			FileUploadUtil.removeDir(brandDir);

			redirectAttributes.addFlashAttribute("message", "The Brand id " + id + " have been deleted successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/export/csv")
	public void exportToCSV(
			HttpServletResponse response) throws IOException {
		List<Brand> list = brandService.ListAll();
		BrandCSVExport export = new BrandCSVExport();
		export.export(list, response);
	}
}
