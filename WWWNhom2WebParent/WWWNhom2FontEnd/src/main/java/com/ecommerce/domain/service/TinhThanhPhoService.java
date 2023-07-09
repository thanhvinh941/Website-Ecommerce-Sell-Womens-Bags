package com.ecommerce.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.common.entity.TinhThanhPho;
import com.ecommerce.domain.repository.TinhThanhPhoRepository;

@Service
public class TinhThanhPhoService {

	@Autowired
	private TinhThanhPhoRepository tinhThanhPhoRepository;
	
	public List<TinhThanhPho> getAll() {
		return (List<TinhThanhPho>) tinhThanhPhoRepository.findAll();
	}

}
