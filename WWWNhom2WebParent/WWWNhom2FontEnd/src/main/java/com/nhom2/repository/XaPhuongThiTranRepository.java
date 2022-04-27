package com.nhom2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.nhom2.common.entity.XaPhuongThiTran;

public interface XaPhuongThiTranRepository extends CrudRepository<XaPhuongThiTran, String>{
	
	@Query("SELECT x FROM XaPhuongThiTran x WHERE x.quanHuyen.maqh = ?1")
	public List<XaPhuongThiTran> getAllByQuanHuyenId(String maqh);

}
