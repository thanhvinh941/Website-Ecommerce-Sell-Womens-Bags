package com.ecommerce.admin.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.admin.repository.OrderRepository;
import com.ecommerce.common.entity.Order;

@Service
@Transactional
public class OrderService {

	public final int CUS_PER_PAGE = 4;
	
	@Autowired
	private OrderRepository orderRepository;

	public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, CUS_PER_PAGE, sort);
		if(keyword != null) {
			return orderRepository.findAll(keyword, pageable);
		}
		return orderRepository.findAll(pageable);
	}

	public List<Order> listAll() {
		return (List<Order>) orderRepository.findAll();
	}

	public long getCount() {
		return orderRepository.count();
	}

}
