package com.ecommerce.admin.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ecommerce.admin.repository.OrderDetailRepository;

@Service
@Transactional
public class OrderDetailService {

	@Autowired
	private OrderDetailRepository detailRepository;
	
	public List<Object[]> getTopTenProductByYearMounth() {
		Page<Object[]> arcusts = detailRepository.getTopTenProductsByMounthYear(PageRequest.of(0, 10));
		return arcusts.getContent();
	}

}
