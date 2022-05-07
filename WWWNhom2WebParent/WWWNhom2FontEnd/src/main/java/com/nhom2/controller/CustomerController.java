package com.nhom2.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.nhom2.common.entity.Address;
import com.nhom2.common.entity.Customer;
import com.nhom2.common.entity.Order;
import com.nhom2.common.entity.OrderDetail;
import com.nhom2.model.CartItemDTO;
import com.nhom2.service.AddressService;
import com.nhom2.service.CartItemService;
import com.nhom2.service.CustomerService;
import com.nhom2.service.OrderDetailService;
import com.nhom2.service.OrderService;

@Controller
public class CustomerController {

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderDetailService orderDetailService;

	@Autowired
	private CartItemService cartItemService;

	@GetMapping("/customers/view-cart")
	public String customerCart(
			HttpServletRequest request, 
			Model model) {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		List<CartItemDTO> lisCartItems = cartItemService.getListCartByCusId(customer.getId());
		model.addAttribute("lisCartItems", lisCartItems);
		return "/account/cart";
	}

	@GetMapping("/customers/check-out")
	public String customerCheckout(
			HttpServletRequest request, 
			Model model) {
		long subTotal=0; long shiping=0;
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		List<CartItemDTO> lisCartItems = cartItemService.getListCartByCusId(customer.getId());
		for(CartItemDTO cartItemDTO : lisCartItems) {
			subTotal += cartItemDTO.getDiscountPrice();
		}
		Address address = addressService.getAddressEnabled();
		shiping = address.getXaPhuong().getShipping();
		Order order = new Order();
		model.addAttribute("lisCartItems", lisCartItems);
		model.addAttribute("order", order);
		model.addAttribute("subTotal", subTotal);
		model.addAttribute("shiping", shiping);
		model.addAttribute("total", (long)(subTotal + shiping));
		model.addAttribute("address", address.getAddressline());
		model.addAttribute("phoneNumber", customer.getPhoneNumber());
		return "/account/checkout";
	}

	@PostMapping("/customers/check-out")
	public String checkOut(
			HttpServletRequest request, 
			Model model, 
			Order order) throws Exception {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		List<CartItemDTO> lisCartItems = cartItemService.getListCartByCusId(customer.getId());
		Order orderCreated = orderService.created(order,customer);
		List<OrderDetail> orderDetails = (List<OrderDetail>) orderDetailService.createdList(orderCreated, lisCartItems);
		orderService.addOrderDetail(orderCreated, orderDetails);
		cartItemService.deleteAllByCus(customer);
		return "redirect:/";
	}
}
