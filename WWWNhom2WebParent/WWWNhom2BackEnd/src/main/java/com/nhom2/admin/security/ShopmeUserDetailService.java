package com.nhom2.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nhom2.admin.repository.UserRepository;
import com.nhom2.common.entity.User;

public class ShopmeUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.getUserByEmail(email);
		if(user != null) {
			return new ShopmeUserDetail(user);
		}
		
		throw new UsernameNotFoundException("Cound not find user with email:" + email);
	}

}
