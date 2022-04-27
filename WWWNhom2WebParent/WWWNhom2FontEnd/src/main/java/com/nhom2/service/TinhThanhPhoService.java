package com.nhom2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom2.common.entity.TinhThanhPho;
import com.nhom2.repository.TinhThanhPhoRepository;

@Service
public class TinhThanhPhoService {

	@Autowired
	private TinhThanhPhoRepository tinhThanhPhoRepository;
	
	public List<TinhThanhPho> getAll() {
		return (List<TinhThanhPho>) tinhThanhPhoRepository.findAll();
	}

}
