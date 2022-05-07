package com.nhom2;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.nhom2.common.entity.Category;
import com.nhom2.common.entity.Customer;
import com.nhom2.common.entity.Product;
import com.nhom2.common.entity.User;
import com.nhom2.exception.CustomerNotFoundException;
import com.nhom2.security.CustomerDetail;
import com.nhom2.service.CategoryService;
import com.nhom2.service.CustomerService;
import com.nhom2.service.ProductService;

@Controller
public class MainController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired 
	private ProductService productService;
	
	@Autowired 
	private CustomerService customerService;
	
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
		System.out.println(customer);
		model.addAttribute("pageTitle", "Forgot Password");	
		return "forgot_password";
	}
	
	@GetMapping("/reset-password")
	public String resetPasswordPage(@Param("token") String token, Model model ) {
		System.out.println(token);
		Customer customer = customerService.getByRestPasswordToken(token);
		System.out.println(customer);
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
			
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle","Invalid Token");
			model.addAttribute("message", e.getMessage());
			return "message";
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
		String toAddress = customer.getEmail();
		String content = "<span style=\"font-size: 18px;\">Gửi [[name]],<br><br></span>\r\n"
				+ "		<div>\r\n"
				+ "			<span style=\"font-size: 18px;\">Nhấn vào đường dẫn bên dưới để xác thực tài khoản bạn đã đăng ký!!!</span>\r\n"
				+ "			<div>\r\n"
				+ "				<span style=\"font-size: 18px;\"><br></span>\r\n"
				+ "			</div>\r\n"
				+ "			<div style=\"text-align: center;\">\r\n"
				+ "				<button style=\"padding: 10px 20px; background-color: blue\">\r\n"
				+ "					<a style=\"font-size: 18px; color: white;\" href=\"[[URL]]\" target=\"_self\">Xác thực</a>\r\n"
				+ "				</button>\r\n"
				+ "			</div>\r\n"
				+ "			<div>\r\n"
				+ "				<span style=\"font-size: 18px;\"><br></span>\r\n"
				+ "			</div>\r\n"
				+ "			<div>\r\n"
				+ "				<span style=\"font-size: 18px;\"><br>Cảm ơn bạn, Nhóm 2 WWW (2021-2022).</span>\r\n"
				+ "				<span style=\"font-size: 18px;\"></span>\r\n"
				+ "			</div>\r\n"
				+ "		</div>";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
		helper.setSubject("Hỗ trợ shopme Nhóm 2 WWW java (2021-2022)");
		helper.setTo(toAddress);
		content = content.replace("[[name]]", customer.getFullName());
		String verifyURL = "http://localhost/Nhom2/verify/" + customer.getVerificationCode();
		content = content.replace("[[URL]]", verifyURL);
		helper.setText(content, true);
		mailSender.send(message);
	}
	
	private void sendVerificationForgotPassword(HttpServletRequest request, String token, String email) throws UnsupportedEncodingException, MessagingException {
		JavaMailSenderImpl mailSender = Utility.prepareMailSender();
		String content = "<span style=\"font-size: 18px;\">Xin chào,<br><br></span>\r\n"
				+ "		<div>\r\n"
				+ "			<span style=\"font-size: 18px;\">Đây là đường dẫn để reset password!!!</span>\r\n"
				+ "			<div>\r\n"
				+ "				<span style=\"font-size: 18px;\"><br></span>\r\n"
				+ "			</div>\r\n"
				+ "			<span style=\"font-size: 18px;\">Nhấn vào đường dẫn để reset password!!!</span>\r\n"
				+ "			<div>\r\n"
				+ "				<span style=\"font-size: 18px;\"><br></span>\r\n"
				+ "			</div>\r\n"
				+ "			<div style=\"text-align: center;\">\r\n"
				+ "				<button style=\"padding: 10px 20px; background-color: blue\">\r\n"
				+ "					<a style=\"font-size: 18px; color: white;\" href=\"[[URL]]\" target=\"_self\">Xác thực</a>\r\n"
				+ "				</button>\r\n"
				+ "			</div>\r\n"
				+ "			<div>\r\n"
				+ "				<span style=\"font-size: 18px;\"><br></span>\r\n"
				+ "			</div>\r\n"
				+ "			<div>\r\n"
				+ "				<span style=\"font-size: 18px;\"><br>Cảm ơn bạn, Nhóm 2 WWW (2021-2022).</span>\r\n"
				+ "				<span style=\"font-size: 18px;\"></span>\r\n"
				+ "			</div>\r\n"
				+ "		</div>";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
		helper.setSubject("Hỗ trợ shopme Nhóm 2 WWW java (2021-2022)");
		helper.setTo(email);
		String verifyURL = "http://localhost/Nhom2/reset-password?" + token;
		content = content.replace("[[URL]]", verifyURL);
		helper.setText(content, true);
		mailSender.send(message);
	}
}
