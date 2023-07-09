package com.ecommerce.admin.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.admin.repository.CustomerRepository;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.User;

@Service
@Transactional
public class CustomerService {
	
	public final int CUS_PER_PAGE = 4;
	
	@Autowired
	private CustomerRepository customerRepository;

	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir , String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, CUS_PER_PAGE, sort);
		
		if(keyword != null) {
			return customerRepository.findAll(keyword, pageable);
		}
		return customerRepository.findAll(pageable);
	}

	public List<Customer> listAll() {
		// TODO Auto-generated method stub
		return (List<Customer>) customerRepository.findAll();
	}

	public long getCount() {
		return customerRepository.count();
	}
	
	
}
