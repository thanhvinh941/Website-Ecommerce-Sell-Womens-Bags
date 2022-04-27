package com.nhom2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nhom2.common.entity.CartItem;
import com.nhom2.common.entity.Customer;
import com.nhom2.common.entity.Product;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {

	@Query("SELECT cart FROM CartItem cart WHERE cart.customer.id = ?1")
	@JsonIgnore
	public List<CartItem> getListCartByCustomerId(Integer cusID);

	public CartItem findByCustomerAndProduct(Customer customer, Product product);

	@Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.customer.id =?2 AND c.product.id =?3")
	@Modifying
	public void updateQuantity(Integer quantity, Integer customer, Integer productId);

	@Query("DELETE FROM CartItem c WHERE c.customer.id =?1")
	@Modifying
	public void deleteByCusId(Integer id);

}
