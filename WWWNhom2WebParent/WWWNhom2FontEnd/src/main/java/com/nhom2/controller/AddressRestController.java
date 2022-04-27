package com.nhom2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.nhom2.common.entity.Address;
import com.nhom2.common.entity.QuanHuyen;
import com.nhom2.common.entity.TinhThanhPho;
import com.nhom2.common.entity.XaPhuongThiTran;
import com.nhom2.service.AddressService;
import com.nhom2.service.QuanHuyenService;
import com.nhom2.service.TinhThanhPhoService;
import com.nhom2.service.XaPhuongThiTranService;

@RestController
public class AddressRestController {
	
	@Autowired
	private TinhThanhPhoService tinhThanhPhoService;
	
	@Autowired
	private QuanHuyenService quanHuyenService;
	
	@Autowired
	private XaPhuongThiTranService xaPhuongThiTranService;
	
	
	@GetMapping("/address/tinh-tp/")
	public List<TinhThanhPho> getTinhTp(){
		return tinhThanhPhoService.getAll();
	}
	
	@GetMapping("/address/tinh-tp/{matp}")
	public List<QuanHuyen> getQuanHuyenByTP(
			@PathVariable(name="matp") String matp){
		return quanHuyenService.getAllByTinhId(matp);
	}
	
	@GetMapping("/address/quan-huyen/{maqh}")
	public List<XaPhuongThiTran> getXaPhuongByQH(
			@PathVariable(name="maqh") String maqh){
		return xaPhuongThiTranService.getAllByQuanHuyenId(maqh);
	}
}
