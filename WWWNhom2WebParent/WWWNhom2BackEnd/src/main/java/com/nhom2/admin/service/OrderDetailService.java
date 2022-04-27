package com.nhom2.admin.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom2.admin.repository.OrderDetailRepository;

@Service
@Transactional
public class OrderDetailService {

	@Autowired
	private OrderDetailRepository detailRepository;
	
	public List<Object[]> getTopTenProductByYearMounth(int year, int mounth1, int mounth2) {
		// TODO Auto-generated method stub
		return detailRepository.getTopTenProductsByMounthYear(year, mounth1, mounth2);
	}

}
