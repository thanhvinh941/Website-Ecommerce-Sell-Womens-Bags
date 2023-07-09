package com.ecommerce.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecommerce.common.entity.OrderDetail;

public interface OrderDetailRepository extends CrudRepository<OrderDetail, Integer>{

}
