package com.ecommerce.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.common.entity.QuanHuyen;
import com.ecommerce.domain.repository.QuanHuyenRepository;

@Service
public class QuanHuyenService {

	@Autowired
	private QuanHuyenRepository quanHuyenRepository;
	
	public List<QuanHuyen> getAllByTinhId(String matp) {
		return (List<QuanHuyen>) quanHuyenRepository.findAllById(matp);
	}

	public List<QuanHuyen> getAll() {
		return (List<QuanHuyen>) quanHuyenRepository.findAll();
	}

}
