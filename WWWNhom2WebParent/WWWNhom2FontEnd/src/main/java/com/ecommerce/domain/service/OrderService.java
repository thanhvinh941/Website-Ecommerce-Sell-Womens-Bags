package com.ecommerce.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.Order;
import com.ecommerce.common.entity.OrderDetail;
import com.ecommerce.domain.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	public Order created(Order order, Customer customer) {
		order.setEnabled(true);
		order.setUpdatedTime(new Date());
		order.setCustomer(customer);
		return orderRepository.save(order);
	}
	
	public Order addOrderDetail(Order order,List<OrderDetail> orderDetails) {
		order.setListOrderDetail(orderDetails);
		return orderRepository.save(order);
	}
	
	public List<Order> getOrderByCusId(Integer cusID){
		return orderRepository.getOrderByCusId(cusID);
	}
}
