package com.ecommerce.domain.service;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.User;
import com.ecommerce.domain.repository.CustomerRepository;
import com.ecommerce.exception.CustomerNotFoundException;

import net.bytebuddy.utility.RandomString;

import javax.servlet.http.HttpServletRequest;

@Service
public class CustomerService {

	@Autowired 
	private CustomerRepository customerRepository;

	@Autowired
	public PasswordEncoder passwordEncoder;
	
	public Customer save(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		String randomcode = RandomString.make(64);
		customer.setVerificationCode(randomcode);
		Customer customerSave = customerRepository.save(customer);
		return customerSave;
	}

	private void encodePassword(Customer customer) {
		String encodePassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodePassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		Customer customerByEmail = customerRepository.getCustomerByEmail(email);
		if (customerByEmail == null)
			return true;

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (customerByEmail != null)
				return false;
		} else {
			if (customerByEmail.getId() != id) {
				return false;
			}
		}

		return true;
	}
	
	public boolean verify(String verificationCode) {
		Customer customer = customerRepository.findByVerificationCode(verificationCode);
		if(customer != null && !customer.isEnabled()) {
			customerRepository.enabled(customer.getId());
			return true;
		}else {
			return false;
		}
	}

	public Customer getCusByEmail(String email) {
		return customerRepository.getCustomerByEmail(email);
	}

	public String getEmailOfAuthenticatedCustomer(HttpServletRequest request){
		Object principal = request.getUserPrincipal();
		String customerEmail = null;

		if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken){
			customerEmail = request.getUserPrincipal().getName();
		}

		return customerEmail;
	}

	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		Customer customer = customerRepository.getCustomerByEmail(email);
		if(customer != null) {
			String token = RandomString.make(30);
			customer.setResetPasswordToken(token);
			customerRepository.save(customer);
			
			return token;
		}else {
			throw new CustomerNotFoundException("cound not find customer with email "+ email);
		}
	}
	
	public Customer getByRestPasswordToken(String token) {
		System.out.println(customerRepository.findByResetPasswordToken(token));
		return customerRepository.findByResetPasswordToken(token);
	}
	
	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByResetPasswordToken(token);
		if (customer == null) {
			throw new CustomerNotFoundException("Invalid Token");
		}
		customer.setPassword(newPassword);
		encodePassword(customer);
		customerRepository.save(customer);
	}
}
