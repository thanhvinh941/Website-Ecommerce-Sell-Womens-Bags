package com.nhom2.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nhom2.common.entity.Brand;
import com.nhom2.common.entity.Product;
import com.nhom2.exception.ProductNotFoundException;
import com.nhom2.repository.ProductRepository;

@Service
public class ProductService {

	public static final int PRODUCT_PER_PAGE = 8;

	@Autowired
	private ProductRepository productRepository;

	public Page<Product> listByCategory(int pageNum, Integer categoryId) {
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);

		return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
	}

	public Product getProduct(String alias) throws Exception {
		Product product = productRepository.findByAliasEnabled(alias);
		if(product == null) {
			throw ProductNotFoundException("Cound not find any product with alias " + alias);
		}
		return product;
	}
	
	public List<Product> listAll() {
		return (List<Product>) productRepository.findAll();
	}

	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId) {
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE, sort);

		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				String categoryInMatch = "-" + String.valueOf(categoryId) + "-";
				return productRepository.searchInCategory(categoryId, categoryInMatch, keyword, pageable);
			}
			return productRepository.findAll(keyword, pageable);
		}

		if (categoryId != null && categoryId > 0) {
			String categoryInMatch = "-" + String.valueOf(categoryId) + "-";
			return productRepository.findAllInCategory(categoryId, categoryInMatch, pageable);
		}

		return productRepository.findAll(pageable);
	}

	public Product get(Integer id) throws Exception {
		try {
			return productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw ProductNotFoundException("Cound not find any product with ID " + id);
		}
	}

	public Page<Product> listProduct(String keyword, int pageNum){
		Sort sort = Sort.by("name");
		sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE, sort);
		if (keyword != null && !keyword.isEmpty()) {
			return productRepository.search(keyword, pageable);
		}		
		return productRepository.findAll(pageable);
	}
	
	private Exception ProductNotFoundException(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Product> getTopProduct() {
		Page<Product> listPages = productRepository.getListProductTop(PageRequest.of(0, 8));
		List<Product> listProducts = listPages.getContent();
		return listProducts;
	}
	
	public List<Product> getListProduct() {
		Page<Product> listPages = productRepository.getListProduct(PageRequest.of(0, 12));
		List<Product> listProducts = listPages.getContent();
		return listProducts;
	}
}
