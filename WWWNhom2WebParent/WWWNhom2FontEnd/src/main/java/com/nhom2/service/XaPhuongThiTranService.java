package com.nhom2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom2.common.entity.XaPhuongThiTran;
import com.nhom2.repository.XaPhuongThiTranRepository;

@Service
public class XaPhuongThiTranService {

	@Autowired
	private XaPhuongThiTranRepository xaPhuongThiTranRepository;

	public List<XaPhuongThiTran> getAllByQuanHuyenId(String maqh) {
		return xaPhuongThiTranRepository. getAllByQuanHuyenId(maqh);
	}

	public List<XaPhuongThiTran> getAll() {
		return (List<XaPhuongThiTran>) xaPhuongThiTranRepository.findAll();
	}

	public XaPhuongThiTran get(String xaPhuongThiTran) {
		return xaPhuongThiTranRepository.findById(xaPhuongThiTran).get();
	}

}
