package com.nhom2.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom2.common.entity.CartItem;
import com.nhom2.common.entity.Customer;
import com.nhom2.common.entity.Product;
import com.nhom2.model.CartItemDTO;
import com.nhom2.repository.CartItemRepository;

@Service
@Transactional
public class CartItemService {

	@Autowired 
	private CartItemRepository cartItemRepository;
	
	@Autowired 
	private ProductService productService;
	
	@SuppressWarnings("null")
	public List<CartItemDTO> getListCartByCusId(Integer cusID){
		List<CartItem> cartItems = cartItemRepository.getListCartByCustomerId(cusID);
		List<CartItemDTO> cartItemDTOs = new ArrayList<CartItemDTO>();
		for(CartItem item : cartItems) {
			cartItemDTOs.add(new CartItemDTO(item.getProduct().getId(),item.getProduct().getName(),item.getProduct().getMainImagePath(),item.getProduct().getAlias(),item.getProduct().getPrice(),item.getProduct().getDiscountPercent(),item.getProduct().getDiscountPrice(),item.getQuantity()));
		}
		return cartItemDTOs;
	}
	
	public Integer addProduct(Integer productId,Integer quantity,Customer customer) {
		Integer updateQuantity = quantity;
		Product product = new Product(productId);
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
		 if(cartItem != null) {
			 updateQuantity = cartItem.getQuantity() + quantity;
			 cartItem.setQuantity(updateQuantity);
		 }else {
			 cartItem = new CartItem();
			 cartItem.setCustomer(customer);
			 cartItem.setProduct(product);
		 }
		 cartItem.setQuantity(updateQuantity);
		 cartItemRepository.save(cartItem);
		 
		 return updateQuantity;
	}
	
	public void deleteProduct(Integer productId, Customer customer) {
		Product product = new Product(productId);
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
		cartItemRepository.delete(cartItem);
	}

	public void updateProduct(Integer productId, Integer quantity, Customer customer) {
		cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
	}

	public void deleteAllByCus(Customer customer) {
		cartItemRepository.deleteByCusId(customer.getId());
	}
}
