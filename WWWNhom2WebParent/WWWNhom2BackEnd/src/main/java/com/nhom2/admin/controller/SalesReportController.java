package com.nhom2.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SalesReportController {

	@GetMapping("/report")
	public String getSalesReportPage() {
		return "sales-report/sales-report";
	}
}
