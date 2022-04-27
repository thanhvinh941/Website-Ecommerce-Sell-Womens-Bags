package com.nhom2.service;

import java.util.List;import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nhom2.common.entity.Brand;
import com.nhom2.exception.BrandNotFoundException;
import com.nhom2.repository.BrandRepository;

@Service
public class BrandService {

	public static final int BRANDS_PER_PAGE = 4;
	
	@Autowired
	private BrandRepository brandRepository;
	
	public List<Brand> ListAll(){
		return (List<Brand>) brandRepository.findAll();
	}
	
	public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1 , BRANDS_PER_PAGE, sort);
		
		if(keyword != null) {
			return brandRepository.findAll(keyword, pageable);
		}
		return brandRepository.findAll(pageable);
	}
	
	public Brand save(Brand brand) {
		return brandRepository.save(brand);
	}
	
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (Exception e) {
			throw new BrandNotFoundException("Cound not find any brand with ID: " + id);
		}
	}
	
	public void delete(Integer id) throws BrandNotFoundException {
		Long counById = brandRepository.countById(id);
		
		if(counById == null || counById == 0) {
			throw new BrandNotFoundException("Cound not find any brand with ID: " + id);
		}
		
		brandRepository.deleteById(id);
	}
	
	public String checkUnique(Integer id,String name) {
		boolean isCreatedNew = (id == null || id ==0);
		Brand brandByName = brandRepository.findByName(name);
		
		if(isCreatedNew) {
			if(brandByName != null) return "Duplicate";
		}else {
			if(brandByName != null && brandByName.getId() != id) return "Duplicate";
		}
		
		return "OK";
	}
}
