package com.nhom2.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhom2.common.entity.Customer;
import com.nhom2.service.CartItemService;
import com.nhom2.service.CustomerService;

@Controller
public class CartItemController {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartItemService cartItemService;
	
	@GetMapping("/add-to-cart/{productId}/{quantity}")
	public String addProductToCart(
			@PathVariable(name = "productId") Integer productId,
			@PathVariable(name = "quantity") Integer quantity, 
			HttpServletRequest request) {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		cartItemService.addProduct(productId, quantity, customer);
		return "redirect:/";
	}
	
	@GetMapping("/delete-to-cart/{productId}")
	public String deleteProductToCart(
			@PathVariable(name = "productId") Integer productId,
			HttpServletRequest request) {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		cartItemService.deleteProduct(productId, customer);
		return "redirect:/";
	}
	
	@GetMapping("/update-to-cart/{productId}/{quantity}")
	public String updateProductToCart(
			@PathVariable(name = "productId") Integer productId,
			@PathVariable(name = "quantity") Integer quantity, 
			@Param("status") String status, 
			HttpServletRequest request) {
		if(status.equals("up")) {
			quantity++;
		}else {
			quantity--;
		}
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		cartItemService.updateProduct(productId, quantity, customer);
		return "redirect:/";
	}
}
