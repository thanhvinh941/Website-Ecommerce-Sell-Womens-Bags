package com.nhom2.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.nhom2.common.entity.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {
	
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.id,' ',c.phoneNumber,' ',c.email,' ',c.firstname,' ',c.lastname)  LIKE %?1%")
	Page<Customer> findAll(String keyword, Pageable pageable);
}
