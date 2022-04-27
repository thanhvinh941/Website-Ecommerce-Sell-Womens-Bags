package com.nhom2.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhom2.admin.service.BrandService;
import com.nhom2.admin.service.CategoryService;
import com.nhom2.admin.service.CustomerService;
import com.nhom2.admin.service.OrderService;
import com.nhom2.admin.service.ProductService;
import com.nhom2.admin.service.UserService;
import com.nhom2.common.entity.Product;
import com.nhom2.common.entity.User;


@Controller
public class MainController {

	@Autowired
	private UserService userService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private OrderService orderService;
	
	@GetMapping("")
	public String viewHomePage(HttpServletRequest request, Model model) {
		String email = userService.getEmailOfAuthenticatedUser(request);
		User user = userService.getUserByEmail(email);
		long users = userService.getCount();
		long brands = brandService.getCount();
		long categories = categoryService.getCount();
		long products = productService.getCount();
		long customers = customerService.getCount();
		long orders = orderService.getCount();
		model.addAttribute("user", user);
		model.addAttribute("brands", brands);
		model.addAttribute("categories", categories);
		model.addAttribute("customers", customers);
		model.addAttribute("products", products);
		model.addAttribute("orders", orders);
		model.addAttribute("users", users);
		return "index";
	}
}
