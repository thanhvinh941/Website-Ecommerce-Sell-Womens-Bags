package com.nhom2.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom2.common.entity.Address;
import com.nhom2.common.entity.Customer;
import com.nhom2.repository.AddressRepository;

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
