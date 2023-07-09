package com.ecommerce.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.admin.category.CategoryDTO;
import com.ecommerce.admin.exception.BrandNotFoundException;
import com.ecommerce.admin.exception.BrandNotFoundResTException;
import com.ecommerce.admin.service.BrandService;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;

@RestController
class BrandRestController {

	@Autowired
	private BrandService brandService;

	@PostMapping("/brands/check_unique")
	public String checkUnique(
			@Param("id") Integer id, 
			@Param("name") String name) {
		return brandService.checkUnique(id, name);	
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> lisCategoryDTOsByBrand(
			@PathVariable(name="id") Integer brandId) throws BrandNotFoundResTException{
		List<CategoryDTO> dtos = new ArrayList<CategoryDTO>();
		
		try {
			Brand brand = brandService.get(brandId);
			Set<Category> categories = brand.getCategories();
			for(Category category : categories) {
				CategoryDTO categoryDTO =  new CategoryDTO(category.getId(), category.getName());
				dtos.add(categoryDTO);
			}
			
			return dtos;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundResTException();
		}
	}
}
