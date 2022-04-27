package com.nhom2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.nhom2.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	@Query("SELECT p FROM Product p WHERE p.enabled = true"
			+ " AND (p.category.id=?1 OR p.category.allParentIDs LIKE %?2%)"
			+ " ORDER BY p.name ASC")
	public Page<Product> listByCategory(Integer categoryId,String categoryMatch,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true AND p.alias=?1")
	public Product findByAliasEnabled(String alias);
	
	public Product findByName(String name);

	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " + " OR p.shortDescription LIKE %?1%"
			+ " OR p.fullDescription LIKE %?1%")
	public Page<Product> search(String keyword, Pageable pageable);

	public Long countById(Integer id);

	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " + " OR p.shortDescription LIKE %?1%"
			+ " OR p.fullDescription LIKE %?1%" + " OR p.brand.name LIKE %?1%" + " OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 " + "OR p.category.allParentIDs LIKE %?2%")
	public Page<Product> findAllInCategory(Integer categoryId, String categoryInMatch, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE ( p.category.id = ?1 " 
			+ "OR p.category.allParentIDs LIKE %?2% ) AND " 
			+ "( p.name LIKE %?3% " 
			+ " OR p.shortDescription LIKE %?3%"
			+ " OR p.fullDescription LIKE %?3%" 
			+ " OR p.brand.name LIKE %?3%" 
			+ " OR p.category.name LIKE %?3%)") 
	public Page<Product> searchInCategory(Integer categoryId, String categoryInMatch, String keyword, Pageable pageable);
	
	@Query("SELECT od.product FROM OrderDetail od GROUP BY od.product") 
	public Page<Product> getListProductTop(Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true") 
	public Page<Product> getListProduct(Pageable pageable);
}
