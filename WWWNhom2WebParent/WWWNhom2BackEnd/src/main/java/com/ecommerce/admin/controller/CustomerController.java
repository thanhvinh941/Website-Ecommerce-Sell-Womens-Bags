package com.ecommerce.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.admin.export.CategoryCSVExport;
import com.ecommerce.admin.export.CustomerCSVExport;
import com.ecommerce.admin.service.CustomerService;
import com.ecommerce.admin.service.UserService;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.User;

@Controller
public class CustomerController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/customers")
	public String listFirstPage(
			HttpServletRequest request,
			Model model) {
		return listByPage(request, 1, "id", "asc", null ,model);
	}

	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(
			HttpServletRequest request,
			@PathVariable(name = "pageNum") int pageNum, 
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir, 
			@Param("keyword") String keyword, 
			Model model) {
		String email = userService.getEmailOfAuthenticatedUser(request);
		User user = userService.getUserByEmail(email);
		
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		Page<Customer> page = customerService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Customer> listCustomers = page.getContent();

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		long startCount = (pageNum - 1) * customerService.CUS_PER_PAGE + 1;
		long endCount = startCount + customerService.CUS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String sortOrther = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("user", user);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItem", page.getTotalElements());
		model.addAttribute("listCustomers", listCustomers);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortOrther", sortOrther);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", reverseSortDir);
		
		return "customers/customers";
	}
	
	@GetMapping("/customers/export/csv")
	public void exportToCSV(
			HttpServletResponse response) throws IOException {
		List<Customer> list = customerService.listAll();
		CustomerCSVExport export = new CustomerCSVExport();
		export.export(list, response);
	}
}
