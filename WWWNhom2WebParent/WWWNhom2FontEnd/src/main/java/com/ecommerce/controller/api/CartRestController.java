package com.ecommerce.controller.api;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.service.CartItemService;
import com.ecommerce.domain.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CartRestController {

	private final CartItemService cartItemService;
	
	private final CartService cartService; 
	
	@PostMapping("/loadcart")
	@ResponseBody
	public String getLoadcart() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String userName = userDetails.getUsername();
		String res = cartService.getLoadcart();
		return res;
	}
}
