package com.nhom2.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhom2.admin.export.CategoryCSVExport;
import com.nhom2.admin.export.OrderCSVExport;
import com.nhom2.admin.service.OrderService;
import com.nhom2.common.entity.Category;
import com.nhom2.common.entity.Order;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/orders")
	public String listFirstPage(
			Model model) {
		return listByPage(1, "id", "asc", null ,model);
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, 
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir, 
			@Param("keyword") String keyword, 
			Model model) {
		
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		Page<Order> page = orderService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Order> listOrders = page.getContent();
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		long startCount = (pageNum - 1) * orderService.CUS_PER_PAGE + 1;
		long endCount = startCount + orderService.CUS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String sortOrther = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItem", page.getTotalElements());
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortOrther", sortOrther);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("list", listOrders);
		
		return "orders/orders";
	}
	
	@GetMapping("/orders/export/csv")
	public void exportToCSV(
			HttpServletResponse response) throws IOException {
		List<Order> list = orderService.listAll();
		OrderCSVExport export = new OrderCSVExport();
		export.export(list, response);
	}
}
