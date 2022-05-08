package com.nhom2.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.nhom2.common.entity.Customer;

@Transactional
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c WHERE c.email = :email")
	public Customer getCustomerByEmail(@Param("email") String email);

	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	public Customer findByVerificationCode(String verificationCode);
	
	@Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
	@Modifying
	public void enabled(Integer id);
	
	@Query("SELECT c FROM Customer c WHERE c.resetPasswordToken = :token")
	public Customer findByResetPasswordToken(@Param("token") String token); 
}
