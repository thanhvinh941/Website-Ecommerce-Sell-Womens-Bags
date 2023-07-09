package com.ecommerce.admin.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.security.ShopmeUserDetail;
import com.ecommerce.admin.service.UserService;
import com.ecommerce.common.entity.User;

@Controller
public class AccountController {

	
	@Autowired
	private UserService service;

	@GetMapping("/account")
	public String viewsDetail(
			HttpServletRequest request,
			@AuthenticationPrincipal ShopmeUserDetail loggedUser, 
			Model model) {
		String email = service.getEmailOfAuthenticatedUser(request);
		User user = service.getUserByEmail(email);
		
		model.addAttribute("user", user);	

		return "users/account_form";
	}
	
	@PostMapping("/account/update")
	public String saveUser(
			User user, 
			RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile,
			@AuthenticationPrincipal ShopmeUserDetail loggedUser) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()).replace(" ", "");
			
			System.out.println(user);
			
			user.setPhotos(fileName.trim());
			User savadUser = service.updateAccount(user);
			
			String uploadDir = "user-photos/" + savadUser.getId();
			
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (user.getPhotos().isEmpty())
				user.setPhotos(null);
			service.updateAccount(user);
		}
		redirectAttributes.addFlashAttribute("message", "Your account detail have been updated.");
		
		loggedUser.setFirstName( user.getFirstname());
		loggedUser.setLastName(user.getLastname());
		return "redirect:/account";
	}
}
