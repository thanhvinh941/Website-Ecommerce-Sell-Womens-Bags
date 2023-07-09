package com.ecommerce.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ecommerce.common.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Integer>{

	
	@Query("SELECT o FROM Order o WHERE o.customer.id = ?1 ORDER BY o.updatedTime DESC")
	List<Order> getOrderByCusId(Integer cusID);

}
