package com.nhom2.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom2.common.entity.Order;
import com.nhom2.common.entity.OrderDetail;
import com.nhom2.common.entity.Product;
import com.nhom2.model.CartItemDTO;
import com.nhom2.repository.OrderDetailRepository;

@Service
public class OrderDetailService {

	@Autowired
	private ProductService productService;
	
	@Autowired 
	private OrderDetailRepository orderDetailRepository;
	public List<OrderDetail> createdList(Order orderCreated, List<CartItemDTO> lisCartItems) throws Exception {
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		for(CartItemDTO cartItemDTO : lisCartItems) {
			Product product = productService.getProduct(cartItemDTO.getAlias());
			OrderDetail detail = new OrderDetail(product, orderCreated, cartItemDTO.getQuantity());
			orderDetails.add(detail);
		}
		List<OrderDetail> orderDetailsSave = (List<OrderDetail>) orderDetailRepository.saveAll(orderDetails);
		return orderDetailsSave;
	}

}
