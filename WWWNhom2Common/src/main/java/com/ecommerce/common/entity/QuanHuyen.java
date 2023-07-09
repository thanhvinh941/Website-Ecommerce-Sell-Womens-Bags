package com.ecommerce.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="quanhuyen")
public class QuanHuyen {
	@Id
	private String maqh;
	private String name;
	private String type;
	private int shipping;
	
	@ManyToOne
	@JoinColumn(name="matp")
	private TinhThanhPho tinhThanhPho;

	public QuanHuyen() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMaqh() {
		return maqh;
	}

	public void setMaqh(String maqh) {
		this.maqh = maqh;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TinhThanhPho getTinhThanhPho() {
		return tinhThanhPho;
	}

	public void setTinhThanhPho(TinhThanhPho tinhThanhPho) {
		this.tinhThanhPho = tinhThanhPho;
	}

	public int getShipping() {
		return shipping+tinhThanhPho.getShipping();
	}

	public void setShipping(int shipping) {
		this.shipping = shipping;
	}

	@Override
	public String toString() {
		return this.tinhThanhPho + this.name;
	}
	
	
}
