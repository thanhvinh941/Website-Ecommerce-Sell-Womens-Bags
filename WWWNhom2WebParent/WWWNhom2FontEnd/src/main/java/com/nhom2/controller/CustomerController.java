package com.nhom2.controller;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nhom2.Utility;
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

	@Autowired
	private TemplateEngine templateEngine;
	
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
			Model model) throws MessagingException {
		long subTotal = 0; long shiping = 0;
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		List<CartItemDTO> lisCartItems = cartItemService.getListCartByCusId(customer.getId());
		for(CartItemDTO cartItemDTO : lisCartItems) {
			subTotal += cartItemDTO.getDiscountPrice();
		}
		Address address = addressService.getAddressEnabled(customer.getId());
		if(address != null) {			
			shiping = address.getXaPhuong().getShipping();
			model.addAttribute("address", address.getAddressline());
		}else {
			model.addAttribute("address", null);
		}
		Order order = new Order();
		model.addAttribute("lisCartItems", lisCartItems);
		model.addAttribute("order", order);
		model.addAttribute("subTotal", subTotal);
		model.addAttribute("shiping", shiping);
		model.addAttribute("total", (long)(subTotal + shiping));
		model.addAttribute("phoneNumber", customer.getPhoneNumber());
		
		return "/account/checkout";
	}

	private void confirmOrder(HttpServletRequest request, Customer customer) throws MessagingException {
		JavaMailSenderImpl mailSender = Utility.prepareMailSender();
		
		List<CartItemDTO> lisCartItems = cartItemService.getListCartByCusId(customer.getId());
		long subTotal=0;
		long shiping=0;
		for(CartItemDTO cartItemDTO : lisCartItems) {
			subTotal += cartItemDTO.getDiscountPrice();
		}
		Address address = addressService.getAddressEnabled(customer.getId());
		shiping = address.getXaPhuong().getShipping();
		
		Locale locale = LocaleContextHolder.getLocale();
		
		Context ctx = new Context(locale);
		ctx.setVariable("cartItems", lisCartItems);
		ctx.setVariable("totalPrice", subTotal + shiping);
		ctx.setVariable("shippingAddress", address.getAddressline());
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
		
		String toAddress = customer.getEmail();
		helper.setTo(toAddress);
		helper.setSubject("Hỗ trợ shopme Nhóm 2 WWW java (2021-2022)");
				
		String htmlContent = "";
		htmlContent = templateEngine.process("mails/email_en.html", ctx);
		helper.setText(htmlContent, true);
		mailSender.send(message);
	}

	@PostMapping("/customers/check-out")
	public String checkOut(
			HttpServletRequest request, 
			Model model, 
			Order order) throws Exception {
		String email = customerService.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCusByEmail(email);
		confirmOrder(request, customer);
		List<CartItemDTO> lisCartItems = cartItemService.getListCartByCusId(customer.getId());
		Order orderCreated = orderService.created(order,customer);
		List<OrderDetail> orderDetails = (List<OrderDetail>) orderDetailService.createdList(orderCreated, lisCartItems);
		orderService.addOrderDetail(orderCreated, orderDetails);
		cartItemService.deleteAllByCus(customer);
		

		return "redirect:/";
	}
}
