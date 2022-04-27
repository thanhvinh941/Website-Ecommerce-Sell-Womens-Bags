package com.nhom2.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhom2.admin.category.CategoryDTO;
import com.nhom2.admin.exception.BrandNotFoundException;
import com.nhom2.admin.exception.BrandNotFoundResTException;
import com.nhom2.admin.service.BrandService;
import com.nhom2.common.entity.Brand;
import com.nhom2.common.entity.Category;

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
