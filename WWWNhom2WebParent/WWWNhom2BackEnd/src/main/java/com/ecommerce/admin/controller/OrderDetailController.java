package com.ecommerce.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.admin.service.OrderDetailService;

@RestController
public class OrderDetailController {

	@Autowired
	private OrderDetailService detailService;
	
	@GetMapping("/report/product-top-10")
	public List<Object[]> getTopTenProductByYearMounth(){
		return detailService.getTopTenProductByYearMounth();
	}
	
}
