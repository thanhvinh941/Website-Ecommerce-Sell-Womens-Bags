package com.nhom2.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nhom2.admin.FileUploadUtil;
import com.nhom2.admin.exception.UserNotFoundException;
import com.nhom2.admin.export.UserCsvExporter;
import com.nhom2.admin.export.UserExcelExporter;
import com.nhom2.admin.export.UserPDFExporter;
import com.nhom2.admin.service.UserService;
import com.nhom2.common.entity.Role;
import com.nhom2.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String viewsLoginPage() {
		return "login";
	}
	
	@GetMapping("/users")
	public String listFirstPage(
			HttpServletRequest request,
			Model model) {
		return listByPage(request,1, "id", "asc", null ,model);
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(
			HttpServletRequest request,
			@PathVariable(name = "pageNum") int pageNum, 
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir, 
			@Param("keyword") String keyword, 
			Model model) {
		String email = userService.getEmailOfAuthenticatedUser(request);
		User user = userService.getUserByEmail(email);
		
		Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
		List<User> listUsers = page.getContent();

		long startCount = (pageNum - 1) * UserService.USER_PER_PAGE + 1;
		long endCount = startCount + UserService.USER_PER_PAGE - 1;
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
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortOrther", sortOrther);
		model.addAttribute("keyword", keyword);
		
		return "users/users";
	}

	@GetMapping("/users/new")
	public String newUser(
			Model model) {
		List<Role> listRoles = userService.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", " Created New User");
		return "users/user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(
			User user, 
			RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()).replace(" ", "");
			user.setPhotos(fileName.trim());
			User savadUser = userService.save(user);
			String uploadDir = "user-photos/" + savadUser.getId();
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (user.getPhotos().isEmpty())
				user.setPhotos(null);
			userService.save(user);
		}
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
		
		return getRedirectURLtoUser(user);
	}

	private String getRedirectURLtoUser(User user) {
		String firstParOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstParOfEmail;
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(
			@PathVariable(name = "id") Integer id, 
			RedirectAttributes redirectAttributes, 
			Model model) {

		try {
			User user = userService.getUserById(id);
			List<Role> listRoles = userService.listRoles();

			model.addAttribute("user", user);
			model.addAttribute("pageTitle", " Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);
			return "users/user_form";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/user/delete/{id}")
	public String deleteUser(
			@PathVariable(name = "id") Integer id, 
			RedirectAttributes redirectAttributes,
			Model model) {
		try {
			userService.deleteUserById(id);
			redirectAttributes.addFlashAttribute("message", "The User ID" + id + " has been deleted successfully");
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}

	@GetMapping("/user/{id}/enabled/{status}")
	public String updateEnabledStatus(
			@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean status, 
			RedirectAttributes redirectAttributes) {
		userService.updateUserEnabledStatus(id, status);
		String enabled = status ? "enabled" : "disabled";
		redirectAttributes.addFlashAttribute("message", "The User ID" + id + " has been " + enabled + "");
		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> users = userService.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(users, response);
	}
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> users = userService.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(users, response);
	}
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> users = userService.listAll();
		UserPDFExporter exporter = new UserPDFExporter();
		exporter.export(users, response);
	}
}
