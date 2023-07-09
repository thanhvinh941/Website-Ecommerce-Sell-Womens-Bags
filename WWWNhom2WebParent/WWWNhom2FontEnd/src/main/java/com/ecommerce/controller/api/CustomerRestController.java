package com.ecommerce.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.common.entity.CartItem;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.domain.model.CartItemDTO;
import com.ecommerce.domain.service.CartItemService;
import com.ecommerce.domain.service.CustomerService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CustomerRestController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartItemService cartItemService;

	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(
			@Param("id") Integer id, 
			@Param("email") String email) {
		return customerService.isEmailUnique(id, email) ? "OK" : "Duplicate";
	}

	@GetMapping("/customers/get_email_authen")
	public Integer getEmailCustomerAuthentication(
			HttpServletRequest request) {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		if (customer != null) {
			return customer.getId();
		}
		return null;
	}

	@GetMapping("/customers/get_cart/{id}")
	public List<CartItemDTO> getListCartByCustomerId(
			@PathVariable("id") Integer id) {
		if (id != null) {
			return cartItemService.getListCartByCusId(id);
		}
		return null;
	}

	
}
