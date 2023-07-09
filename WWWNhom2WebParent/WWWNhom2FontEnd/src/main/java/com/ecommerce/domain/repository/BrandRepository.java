package com.ecommerce.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ecommerce.common.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	Page<Brand> findAll(String keyword, Pageable pageable);

	public Long countById(Integer id);

	public Brand findByName(String name);

	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAll();

}
