package com.ecommerce.domain.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecommerce.common.entity.Customer;
import com.ecommerce.domain.repository.CustomerRepository;

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
