package com.ecommerce.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.Product;
import com.ecommerce.domain.service.CategoryService;
import com.ecommerce.domain.service.ProductService;

@Controller
public class ProductController {
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(
			@PathVariable("category_alias") String alias, 
			Model model) {
		return viewCategoryByPage(alias, 1, model);
	};

	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(
			@PathVariable("category_alias") String alias, 
			@PathVariable("pageNum") int pageNum,
			Model model) {
		Category category = categoryService.getCategory(alias);
		if (category == null) {
			return "error/404";
		}
		List<Category> categoryParents = categoryService.getCategoryParents(category);
		Page<Product> pageProduct = productService.listByCategory(pageNum, category.getId());
		List<Product> listProducts = pageProduct.getContent();
		long startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
		if (endCount > pageProduct.getTotalElements()) {
			endCount = pageProduct.getTotalElements();
		}
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPage", pageProduct.getTotalPages());
		model.addAttribute("totalItem", pageProduct.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("pageTitle", category.getName());
		model.addAttribute("categoryParents", categoryParents);
		model.addAttribute("category", category);

		return "products/product_by_category";
	}
	
	@GetMapping("/p/{product_alias}")
	public String viewProductDetail(
			@PathVariable("product_alias") String alias,
			Model model) throws Exception {
		try {			
			Product productDetail = productService.getProduct(alias);
			List<Category> categoryParents = categoryService.getCategoryParents(productDetail.getCategory());
			model.addAttribute("categoryParents", categoryParents);
			model.addAttribute("productDetail", productDetail);
			model.addAttribute("pageTitle", productDetail.getName());

			return "products/product_detail";
		} catch (Exception e) {
			return "error/404";
		} 

	}
	
	@GetMapping("/p/detail-modal/{id}")
	public String detailModalProduct(
			@PathVariable("id") Integer id, 
			Model model, 
			RedirectAttributes redirectAttributes) {
		try {

			Product product = productService.get(id);
			model.addAttribute("product", product);

			return "products/product_detail_modal";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/";
		}
	}
	
}
