package com.ecommerce.domain.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.common.entity.CartItem;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.domain.model.CartItemDTO;
import com.ecommerce.domain.service.CartItemService;

public class CustomerDetail implements UserDetails{

	private Customer customer;
	
	private CartItemService cartItemService;
	
	public CustomerDetail(Customer customer) {
		super();
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return customer.isEnabled();
	}

	public String getFullName() {
		return customer.getFullName();
	}
	
	public List<CartItemDTO> getCartIteams(){
		return cartItemService.getListCartByCusId(customer.getId());
	}
}
