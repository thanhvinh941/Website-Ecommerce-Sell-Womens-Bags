package com.nhom2.model;

public class CartItemDTO {
	private	Integer id;
	private String name;
	private String image;
	private String alias;
	private double price;
	private double discountPercent;
	private double discountPrice;
	private int quantity;

	public CartItemDTO(Integer id, String name, String image, String alias, double price, double discountPercent,
			double discountPrice, int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.alias = alias;
		this.price = price;
		this.discountPercent = discountPercent;
		this.discountPrice = discountPrice;
		this.quantity = quantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
