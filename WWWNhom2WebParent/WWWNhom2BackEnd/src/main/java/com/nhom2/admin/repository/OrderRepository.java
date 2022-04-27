package com.nhom2.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.nhom2.common.entity.Order;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE CONCAT(o.id,' ',o.customer.firstname,' ',o.customer.lastname,' ')  LIKE %?1%")
	Page<Order> findAll(String keyword, Pageable pageable);

	
}
