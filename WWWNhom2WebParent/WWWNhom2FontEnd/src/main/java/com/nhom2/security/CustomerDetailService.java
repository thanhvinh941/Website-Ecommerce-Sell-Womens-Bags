package com.nhom2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nhom2.common.entity.Customer;
import com.nhom2.repository.CustomerRepository;

public class CustomerDetailService implements UserDetailsService{

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = customerRepository.getCustomerByEmail(email);
		if(customer != null) {
			return new CustomerDetail(customer);
		}
		
		throw new UsernameNotFoundException("Cound not find user with email:" + email);
	}
}
