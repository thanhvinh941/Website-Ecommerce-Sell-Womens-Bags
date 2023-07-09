package com.ecommerce.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.common.entity.Address;
import com.ecommerce.common.entity.QuanHuyen;
import com.ecommerce.common.entity.TinhThanhPho;
import com.ecommerce.common.entity.XaPhuongThiTran;
import com.ecommerce.domain.service.AddressService;
import com.ecommerce.domain.service.QuanHuyenService;
import com.ecommerce.domain.service.TinhThanhPhoService;
import com.ecommerce.domain.service.XaPhuongThiTranService;

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
