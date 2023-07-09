package com.ecommerce.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.common.entity.XaPhuongThiTran;
import com.ecommerce.domain.repository.XaPhuongThiTranRepository;

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
