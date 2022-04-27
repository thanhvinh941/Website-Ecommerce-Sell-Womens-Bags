package com.nhom2.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhom2.admin.service.OrderDetailService;

@RestController
public class OrderDetailController {

	@Autowired
	private OrderDetailService detailService;
	
	@GetMapping("/report/product-top-10")
	public List<Object[]> getTopTenProductByYearMounth(){
		int year=2022;
		int mounth1 = 1;
		int mounth2 = 5;
		
		return detailService.getTopTenProductByYearMounth(year,mounth1,mounth2);
	}
	
}
