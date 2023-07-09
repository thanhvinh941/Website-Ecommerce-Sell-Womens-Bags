package com.ecommerce.admin.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.admin.exception.ProductNotFoundException;
import com.ecommerce.admin.repository.ProductRepository;
import com.ecommerce.common.entity.Product;

@Service
public class ProductService {

	public static final int PRODUCT_PER_PAGE = 6;

	@Autowired
	private ProductRepository productRepository;

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
				System.out.println(categoryInMatch);
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

	public Product save(Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}

		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}

		product.setUpdatedTime(new Date());

		return productRepository.save(product);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = productRepository.findByName(name);

		if (isCreatingNew) {
			if (productByName != null)
				return "Duplicate";
		} else {
			if (productByName != null && productByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}

	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		productRepository.updateEnabledStatus(id, enabled);
	}

	public void delete(Integer id) throws ProductNotFoundException {
		Long countById = productRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new ProductNotFoundException("Count not find any product with ID: " + id);
		}

		productRepository.deleteById(id);
	}

	public Product get(Integer id) throws Exception {
		try {
			return productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw ProductNotFoundException("Cound not find any product with ID " + id);
		}
	}

	private Exception ProductNotFoundException(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getCount() {
		return productRepository.count();
	}
}
