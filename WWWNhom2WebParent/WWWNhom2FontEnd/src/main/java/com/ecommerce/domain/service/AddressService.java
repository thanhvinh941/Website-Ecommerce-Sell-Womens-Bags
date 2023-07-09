package com.ecommerce.domain.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.common.entity.Address;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.domain.repository.AddressRepository;

@Service
@Transactional
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;
	
	public List<Address> listAddressByCus(Customer customer){
		List<Address> listAddresses = addressRepository.getAddressByCus(customer.getId());
		return listAddresses;
	}
	
	public Address getAddress(Integer addressId) {
		return addressRepository.findById(addressId).get();
	}
	
	public Address getAddressEnabled(Integer cusID) {
		return addressRepository.findByEnabled(cusID);
	}

	public void update(Address address) {
		if(address.isEnabled()) {
			addressRepository.updateAllAddressDisabled(address.getCustomer().getId());
			addressRepository.save(address);
		}
		addressRepository.save(address);
	}

	public void save(Address address) {
		if(address.isEnabled()) {
			addressRepository.updateAllAddressDisabled(address.getCustomer().getId());
			addressRepository.save(address);
		}
		addressRepository.save(address);		
	}

	public void delete(Address address) {
		addressRepository.delete(address);
	}
}
