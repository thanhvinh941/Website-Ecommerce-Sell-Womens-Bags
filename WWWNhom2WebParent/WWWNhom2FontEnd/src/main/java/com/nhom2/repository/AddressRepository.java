package com.nhom2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.nhom2.common.entity.Address;

public interface AddressRepository extends CrudRepository<Address, Integer>{

	@Query("SELECT a FROM Address a WHERE a.customer.id = ?1 ORDER BY a.enabled DESC")
	public List<Address> getAddressByCus(Integer cusID);

	@Query("UPDATE Address a SET a.enabled = 0 WHERE a.customer.id =?1 ")
	@Modifying
	public void updateAllAddressDisabled(Integer id);

	@Query("SELECT a FROM Address a WHERE a.enabled = 1")
	public Address findByEnabled();
}
