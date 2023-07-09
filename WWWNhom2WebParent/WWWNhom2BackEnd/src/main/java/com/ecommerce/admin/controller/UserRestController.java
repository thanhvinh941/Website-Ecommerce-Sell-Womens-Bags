package com.ecommerce.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.admin.service.UserService;

@RestController
public class UserRestController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/users/check_email")
	public String checkDuplicateEmail(
			@Param("id") Integer id, 
			@Param("email") String email) {
		return userService.isEmailUnique(id, email) ? "OK" :  "Duplicate";
	}
}
