package com.nhom2.model;

public class CartItemDTO {
	private	Integer id;
	private String name;
	private String image;
	private String alias;
	private long price;
	private long discountPercent;
	private long discountPrice;
	private int quantity;

	public CartItemDTO(Integer id, String name, String image, String alias, long price, long discountPercent,
			long discountPrice, int quantity) {
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

	public long getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(long discountPercent) {
		this.discountPercent = discountPercent;
	}

	public long getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(long discountPrice) {
		this.discountPrice = discountPrice;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
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
