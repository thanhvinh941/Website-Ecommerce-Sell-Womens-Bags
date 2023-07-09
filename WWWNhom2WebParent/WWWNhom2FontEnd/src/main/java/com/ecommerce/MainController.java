package com.ecommerce;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.Product;
import com.ecommerce.common.entity.User;
import com.ecommerce.domain.security.CustomerDetail;
import com.ecommerce.domain.service.CategoryService;
import com.ecommerce.domain.service.CustomerService;
import com.ecommerce.domain.service.ProductService;
import com.ecommerce.exception.CustomerNotFoundException;

@Controller
public class MainController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired 
	private ProductService productService;
	
	@Autowired 
	private CustomerService customerService;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		List<Category> listRootCategories = categoryService.getListRootCategories();
		List<Product> listProductsTop = productService.getTopProduct();
		List<Product> listProducts = productService.getListProduct();
		model.addAttribute("listRootCategories", listRootCategories);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("listProductsTop", listProductsTop);
		model.addAttribute("listProducts", listProducts);
		return "index";
	}
	
	@RequestMapping(value = { "/coming-soon"}, method = RequestMethod.GET)
	public String commingSoonPage() {
		return "comming_soon";
	}
	
	@RequestMapping(value = { "/shop-grid"}, method = RequestMethod.GET)
	public String shopGridFirstPage(@Param("sortDir") String sortDir, Model model) {
		return shopGridByPage(1, null, model);
	}
	
	@GetMapping("/shop-grid/page/{pageNum}")
	public String shopGridByPage(@PathVariable(name = "pageNum") int pageNum,
			 @Param("keyword") String keyword, 
			Model model) {
		Page<Product> page = productService.listProduct(keyword, pageNum);
		List<Product> listProducts = page.getContent();
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		long startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("totalItem", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle","Shop grid");
		return "shop_grid";
	}
	
	@GetMapping("/login")
	public String viewsLoginPage(Model model ) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {			
			return "login";
		}
		model.addAttribute("pageTitle", "Login");	
		return "redirect:/";
	}
	
	@GetMapping("/signup")
	public String viewsSignUpPage(Model model ) {
		Customer customer  = new Customer();
		model.addAttribute("customer", customer);
		model.addAttribute("pageTitle", "Sign Up");	
		return "signup";
	}
	
	@GetMapping("/forgot-password")
	public String viewsForgotPasswordPage(Model model ) {
		model.addAttribute("pageTitle", "Forgot Password");	
		return "forgot_password";
	}
	
	@PostMapping("/forgot-password")
	public String getForgotPasswordPage(@PathParam("email") String email, Model model,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		try {
			String token = customerService.updateResetPasswordToken(email);
			sendVerificationForgotPassword(request, token, email);
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		}
		Customer customer = customerService.getCusByEmail(email);
		model.addAttribute("pageTitle", "Forgot Password");	
		return "forgot_password";
	}
	
	@GetMapping("/reset-password")
	public String resetPasswordPage(@Param("token") String token, Model model ) {
		Customer customer = customerService.getByRestPasswordToken(token);
		if(customer != null) {
			model.addAttribute("token", token);
		}else {
			model.addAttribute("pageTitle","Invalid Token");
			model.addAttribute("message", "Invalid Token");
			return "message";
		}
		model.addAttribute("pageTitle", "Forgot Password");	
		return "reset_password";
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(HttpServletRequest request, Model model ) {
		String token = request.getParameter("token");
		String passWord = request.getParameter("password");
		try {
			customerService.updatePassword(token, passWord);
			model.addAttribute("pageTitle","Reset Your password");
			model.addAttribute("title", "Reset Your password.");
			model.addAttribute("message", "You have successfully changed your password.");
			
			return viewsLoginPage(model);
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle","Invalid Token");
			model.addAttribute("message", e.getMessage());
			return viewsForgotPasswordPage(model);
		}
	}
	
	
	@PostMapping("/register")
	public String signUp(Customer customer, RedirectAttributes redirectAttributes, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		Customer customerSave = customerService.save(customer);
		if(customerSave != null) {
			sendVerificationEmail(request, customer);
			return "redirect:/login";
		}
		return "redirect:/signup";
	}

	@GetMapping("/verify/{code}")
	public String verifyAccount(
			@PathVariable("code") String verificationCode, 
			RedirectAttributes redirectAttributes, Model model) {
		boolean verified = customerService.verify(verificationCode);
		redirectAttributes.addFlashAttribute("message", "verificationCode");
		return "redirect:/" + (verified ? "login" : "login?verify=flase");
	}
	
	private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
		JavaMailSenderImpl mailSender = Utility.prepareMailSender();
		
		Locale locale = LocaleContextHolder.getLocale();
		
		Context ctx = new Context(locale);
		
		String verifyURL = "http://localhost/Nhom2/verify/" + customer.getVerificationCode();
		ctx.setVariable("url", verifyURL);
		
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
		
		String toAddress = customer.getEmail();
		helper.setTo(toAddress);
		helper.setSubject("Hỗ trợ shopme Nhóm 2 WWW java (2021-2022)");
				
		String htmlContent = "";
		htmlContent = templateEngine.process("mails/email_registered.html", ctx);
		helper.setText(htmlContent, true);
		mailSender.send(message);
	}
	
	private void sendVerificationForgotPassword(HttpServletRequest request, String token, String email) throws UnsupportedEncodingException, MessagingException {
		JavaMailSenderImpl mailSender = Utility.prepareMailSender();
		
		Locale locale = LocaleContextHolder.getLocale();
		
		Context ctx = new Context(locale);
		
		String verifyURL = "http://localhost/Nhom2/reset-password?token=" + token;
		ctx.setVariable("url", verifyURL);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
		
		helper.setTo(email);
		helper.setSubject("Hỗ trợ shopme Nhóm 2 WWW java (2021-2022)");
				
		String htmlContent = "";
		htmlContent = templateEngine.process("mails/email_confirm_forgotpassword.html", ctx);
		helper.setText(htmlContent, true);
		mailSender.send(message);
	}
}
