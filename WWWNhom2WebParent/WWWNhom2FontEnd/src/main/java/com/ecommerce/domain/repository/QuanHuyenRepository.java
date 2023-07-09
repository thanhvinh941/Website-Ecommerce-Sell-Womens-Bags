package com.ecommerce.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ecommerce.common.entity.QuanHuyen;

public interface QuanHuyenRepository extends CrudRepository<QuanHuyen, String>{

	@Query("SELECT q FROM QuanHuyen q WHERE q.tinhThanhPho.matp = ?1")
	public List<QuanHuyen> findAllById(String matp);

}
