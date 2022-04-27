package com.nhom2.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="xaphuongthitran")
public class XaPhuongThiTran {

	@Id
	private String xaid;
	private String name;
	private String type;
	private int shipping;
	
	@ManyToOne
	@JoinColumn(name="maqh")
	private QuanHuyen quanHuyen;

	public XaPhuongThiTran() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getXaid() {
		return xaid;
	}

	public void setXaid(String xaid) {
		this.xaid = xaid;
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

	public QuanHuyen getQuanHuyen() {
		return quanHuyen;
	}

	public void setQuanHuyen(QuanHuyen quanHuyen) {
		this.quanHuyen = quanHuyen;
	}

	public int getShipping() {
		return shipping + quanHuyen.getShipping();
	}

	public void setShipping(int shipping) {
		this.shipping = shipping;
	}

	@Override
	public String toString() {
		return this.name + this.quanHuyen;
	}
	
	
}
