package com.ecommerce.admin.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.ecommerce.admin.export.ProductCSVExport;
import com.ecommerce.admin.service.BrandService;
import com.ecommerce.admin.service.CategoryService;
import com.ecommerce.admin.service.ProductService;
import com.ecommerce.admin.service.UserService;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.Product;
import com.ecommerce.common.entity.ProductImage;
import com.ecommerce.common.entity.User;

@Controller
public class ProductController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/products")
	public String listFirstPage(
			HttpServletRequest request,
			@Param("sortDir") String sortDir, 
			Model model) {
		return listByPage(request, 1, "id", sortDir, null, 0, model);
	}

	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			HttpServletRequest request,
			@PathVariable(name = "pageNum") int pageNum, 
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir, 
			@Param("keyword") String keyword, 
			@Param("categoryId") Integer categoryId,
			Model model){ 
		String email = userService.getEmailOfAuthenticatedUser(request);
		User user = userService.getUserByEmail(email);
		
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
		List<Product> listProducts = page.getContent();
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		long startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		if(categoryId != null) model.addAttribute("categoryId", categoryId);
		
		model.addAttribute("user", user);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("totalItem", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("listCategories", listCategories);
		return "products/products";
	}

	@GetMapping("/products/new")
	public String newProduct(
			HttpServletRequest request,
			Model model) {
		String email = userService.getEmailOfAuthenticatedUser(request);
		User user = userService.getUserByEmail(email);
		List<Brand> listBrands = brandService.ListAll();
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		product.setId(0);
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("user", user);
		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(
			Product product, 
			@RequestParam("fileImage") MultipartFile multipartFile,
			@RequestParam("extraImage") MultipartFile[] extraMultipartFile,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			RedirectAttributes redirectAttributes) throws IOException {
		
		setMainImagesname(multipartFile, product);
		setExistingExtraImagesname(imageIDs, imageNames, product);
		setExtraImagesname(extraMultipartFile, product);
		setDetails(detailIDs, detailNames, detailValues, product);

		Product saveProduct = productService.save(product);
		saveUploadedImagesFile(multipartFile, extraMultipartFile, saveProduct);
		deleteExtraImagesWeredRemoveOnForm(product);
		redirectAttributes.addFlashAttribute("message", "The product have been save successfully.");
		return "redirect:/products";
	}

	private void deleteExtraImagesWeredRemoveOnForm(Product product) {
		String extraImage = "/products-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImage);

		try {
			Files.list(dirPath).forEach(file -> {
				String filename = file.toFile().getName();

				if (!product.containsImageName(filename)) {
					try {
						Files.delete(file);
					} catch (Exception e) {

					}
				}
			});
		} catch (Exception e) {

		}
	}

	private void setExistingExtraImagesname(String[] imageIDs, String[] imageNames, Product product) {
		if (imageIDs == null || imageIDs.length == 0)
			return;
		Set<ProductImage> images = new HashSet<ProductImage>();
		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];

			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);
	}

	private void setDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
		if (detailNames == null || detailNames.length == 0)
			return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);
			if (id != 0) {
				product.addDetail(id, name, value);
			} else {
				if (!name.isEmpty() && !value.isEmpty()) {
					product.addDetail(name, value);
				}
			}

		}
	}

	private void saveUploadedImagesFile(MultipartFile multipartFile, MultipartFile[] extraMultipartFile,
			Product product) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String uploadDir = "../products-images/" + product.getId();
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}

		if (extraMultipartFile.length > 0) {
			String extraMultipartDir = "../products-images/" + product.getId() + "/extras";

			for (MultipartFile file : extraMultipartFile) {
				if (file.isEmpty())
					continue;
				String fileName = StringUtils.cleanPath(file.getOriginalFilename());
				FileUploadUtil.saveFile(extraMultipartDir, fileName, file);
			}
		}
	}

	private void setExtraImagesname(MultipartFile[] extraMultipartFile, Product product) {
		if (extraMultipartFile.length > 0) {
			for (MultipartFile multipartFile : extraMultipartFile) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
				}
			}
		}
	}

	private void setMainImagesname(MultipartFile multipartFile, Product product) {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			product.setMainImage(fileName);
		}
	};

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(
			@PathVariable("id") Integer id, 
			@PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product Id: " + id + " has  been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/products";
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(
			@PathVariable("id") Integer id, 
			Model model, 
			RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			String extraUploadDir = "../products-images/" + id + "/extras";
			String uploadDir = "../products-images/" + id;
			FileUploadUtil.removeDir(uploadDir);
			FileUploadUtil.removeDir(extraUploadDir);
			redirectAttributes.addFlashAttribute("message", "The products ID " + id + " has been deleted successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(
			HttpServletRequest request,
			@PathVariable("id") Integer id, 
			Model model, 
			RedirectAttributes redirectAttributes) {
		String email = userService.getEmailOfAuthenticatedUser(request);
		User user = userService.getUserByEmail(email);
		try {

			List<Brand> listBrands = brandService.ListAll();
			Product product = productService.get(id);
			
			Integer numberOfExisting = product.getImages().size();
			model.addAttribute("user", user);
			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExisting", numberOfExisting);

			return "products/product_form";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}

	@GetMapping("/products/detail/{id}")
	public String detailProduct(
			HttpServletRequest request,
			@PathVariable("id") Integer id, 
			Model model, 
			RedirectAttributes redirectAttributes) {
		String email = userService.getEmailOfAuthenticatedUser(request);
		User user = userService.getUserByEmail(email);
		try {

			Product product = productService.get(id);
			model.addAttribute("product", product);
			model.addAttribute("user", user);
			return "products/product_detail_modal";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/export/csv")
	public void exportToCSV(
			HttpServletResponse response) throws IOException {
		List<Product> list = productService.listAll();
		ProductCSVExport export = new ProductCSVExport();
		export.export(list, response);
	}
}
