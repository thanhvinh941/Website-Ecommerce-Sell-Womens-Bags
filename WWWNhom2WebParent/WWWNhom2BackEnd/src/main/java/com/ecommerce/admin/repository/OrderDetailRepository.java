package com.ecommerce.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.common.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, Integer>{

	@Query("SELECT count(od.quantity) * od.product.price, od.product.name FROM OrderDetail od where od.order.id in "
			+ "(SELECT o.id FROM Order o)"
			+ " group by od.product.id")
	public Page<Object[]> getTopTenProductsByMounthYear(Pageable pageable);

}
