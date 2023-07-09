package com.ecommerce.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tinhthanhpho")
public class TinhThanhPho {
	@Id
	private String matp;
	private String name;
	private String type;
	private String slug;
	private int shipping;
	
	public TinhThanhPho() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMatp() {
		return matp;
	}

	public void setMatp(String matp) {
		this.matp = matp;
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
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	
	public int getShipping() {
		return shipping;
	}

	public void setShipping(int shipping) {
		this.shipping = shipping;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
}
