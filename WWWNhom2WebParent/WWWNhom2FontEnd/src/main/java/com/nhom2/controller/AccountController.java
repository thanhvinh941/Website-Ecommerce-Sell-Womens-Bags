package com.nhom2.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.core.sym.Name;
import com.nhom2.common.entity.Address;
import com.nhom2.common.entity.Customer;
import com.nhom2.common.entity.Order;
import com.nhom2.common.entity.QuanHuyen;
import com.nhom2.common.entity.TinhThanhPho;
import com.nhom2.common.entity.XaPhuongThiTran;
import com.nhom2.service.AddressService;
import com.nhom2.service.CustomerService;
import com.nhom2.service.OrderService;
import com.nhom2.service.QuanHuyenService;
import com.nhom2.service.TinhThanhPhoService;
import com.nhom2.service.XaPhuongThiTranService;

@Controller
public class AccountController {

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private TinhThanhPhoService tinhThanhPhoService;
	
	@Autowired
	private QuanHuyenService quanHuyenService;
	
	@Autowired
	private XaPhuongThiTranService xaPhuongThiTranService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/customer/account/edit")
	public String getAccountEditView(
			HttpServletRequest request, 
			Model model) {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		model.addAttribute("customer", customer);
		return "account/account_detail";
	}

	@GetMapping("/customer/address")
	public String getAccountAddressView(
			HttpServletRequest request, 
			Model model) {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		List<Address> listAddresses = addressService.listAddressByCus(customer);
		model.addAttribute("listAddresses", listAddresses);
		return "account/location_book";
	}
	
	@GetMapping("/customer/address/new")
	public String addAccountAddressView(
			HttpServletRequest request, 
			Model model) {
		Address address = new Address("", "79", "764", "26887", null, null);
		List<TinhThanhPho> tinhThanhPhos = tinhThanhPhoService.getAll();
		model.addAttribute("address", address);
		model.addAttribute("tinhThanhPhos", tinhThanhPhos);
		return "account/location_book_form";
	}
	
	@GetMapping("/customer/address/edit/{addressId}")
	public String editAccountAddressView(
			HttpServletRequest request, 
			Model model,
			@PathVariable(name="addressId") Integer addressId) {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		Address address = addressService.getAddress(addressId);
		List<TinhThanhPho> tinhThanhPhos = tinhThanhPhoService.getAll();
		List<QuanHuyen> quanHuyens = quanHuyenService.getAllByTinhId(address.getXaPhuong().getQuanHuyen().getTinhThanhPho().getMatp());
		List<XaPhuongThiTran> xaPhuongThiTrans = xaPhuongThiTranService.getAllByQuanHuyenId(address.getXaPhuong().getQuanHuyen().getMaqh());
		model.addAttribute("tinhThanhPhos", tinhThanhPhos);
		model.addAttribute("quanHuyens", quanHuyens);
		model.addAttribute("xaPhuongThiTrans", xaPhuongThiTrans);
		model.addAttribute("addressId", addressId);
		model.addAttribute("customer", customer);
		model.addAttribute("address", address);
		return "account/location_book_form";
	}
	
	@PostMapping("/customer/address/save")
	public String editAccountAddress(
			HttpServletRequest request, 
			Model model,
			@Param("id") Integer id,
			Address address,
			@Param("enabled") boolean enabled) {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		XaPhuongThiTran xaPhuongThiTran = xaPhuongThiTranService.get(address.getXaPhuongThiTran());
		address.setXaPhuong(xaPhuongThiTran);
		address.setCustomer(customer);
		if(address.getId() != null) {
			addressService.update(address);
			return "redirect:/customer/address?action=update";
		}else {
			addressService.save(address);
			return "redirect:/customer/address?action=add";
		}
	}

	@GetMapping("/customer/address/delete/{addressId}")
	public String deleteAccountAddressView(
			HttpServletRequest request, 
			Model model,
			@PathVariable(name="addressId") Integer addressId) {
		Address address = addressService.getAddress(addressId);
		if(address != null) {			
			addressService.delete(address);
			return "redirect:/customer/address";
		}
		return"/error/404";
	}

	@GetMapping("/sales/order/history")
	public String getAccountOrderView(
			HttpServletRequest request, 
			Model model) {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		List<Order> orders = orderService.getOrderByCusId(customer.getId());
		model.addAttribute("orders", orders);
	
		return "account/orders_manager";
	}
}
