package com.nhom2.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "address_line", length = 64)
	private String addressline;

	@Column(name = "tinh_thanh_pho", length = 45)
	private String tinhThanhPho;

	@Column(name = "quan_huyen", length = 45)
	private String quanHuyen;

	@Column(name = "xa_phuong", length = 45)
	private String xaPhuongThiTran;

	@ManyToOne
	@JoinColumn(name = "xaphuong_id")
	private XaPhuongThiTran xaPhuong;
	
	@ManyToOne
	@JoinColumn(name = "cus_id")
	private Customer customer;

	private boolean enabled;

	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Address(String addressline, String tinhThanhPho, String quanHuyen, String xaPhuongThiTran,
			XaPhuongThiTran xaId, Customer customer) {
		super();
		this.addressline = addressline;
		this.tinhThanhPho = tinhThanhPho;
		this.quanHuyen = quanHuyen;
		this.xaPhuongThiTran = xaPhuongThiTran;
		this.customer = customer;
		this.enabled = false;
	}

	public void setXaPhuongThiTran(String xaPhuongThiTran) {
		this.xaPhuongThiTran = xaPhuongThiTran;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddressline() {
		return addressline;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAddressline(String addressline) {
		this.addressline = addressline;
	}

	public String getTinhThanhPho() {
		return tinhThanhPho;
	}

	public void setTinhThanhPho(String tinhThanhPho) {
		this.tinhThanhPho = tinhThanhPho;
	}

	public String getQuanHuyen() {
		return quanHuyen;
	}

	public void setQuanHuyen(String quanHuyen) {
		this.quanHuyen = quanHuyen;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getXaPhuongThiTran() {
		return xaPhuongThiTran;
	}

	public XaPhuongThiTran getXaPhuong() {
		return xaPhuong;
	}

	public void setXaPhuong(XaPhuongThiTran xaPhuong) {
		this.xaPhuong = xaPhuong;
	}

	@Override
	public String toString() {
		return "Address [addressline=" + addressline + ", tinhThanhPho=" + tinhThanhPho + ", quanHuyen=" + quanHuyen
				+ ", xaPhuongThiTran=" + xaPhuongThiTran + ", xaPhuong=" + xaPhuong + ", customer=" + customer
				+ ", enabled=" + enabled + "]";
	}

}
